package com.spm.android.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.inject.internal.Nullable;
import com.spm.R;
import com.spm.android.common.ActivityLauncher;
import com.spm.android.common.AndroidApplication;
import com.spm.android.common.activity.AbstractActivity;
import com.spm.android.common.listener.AsyncOnClickListener;
import com.spm.android.common.listener.LaunchOnClickListener;
import com.spm.android.common.utils.AlertDialogUtils;
import com.spm.android.common.utils.LocalizationUtils;
import com.spm.android.common.utils.ToastUtils;
import com.spm.common.domain.Application;
import com.spm.common.exception.AndroidException;
import com.spm.common.utils.ThreadUtils;
import com.spm.domain.Client;
import com.spm.domain.Product;
import com.spm.domain.User;
import com.spm.domain.Work;
import com.spm.repository.DBUserRepository;
import com.spm.repository.UserRepository;
import com.spm.service.APIServiceImpl;

import org.androidannotations.annotations.EActivity;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import hugo.weaving.DebugLog;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import roboguice.inject.InjectView;

/**
 * @author Agustin Sgarlata
 */
@EActivity
public class DashBoardActivity extends AbstractActivity {

    private UpdateDataUseCase updateDataUseCase;
    private SyncUseCase syncUseCase;

    UserRepository userRepository;

    @Nullable
    @InjectView(R.id.userName)
    private TextView userName;

    @Nullable
    @InjectView(R.id.clientes)
    private Button clientes;

    @Nullable
    @InjectView(R.id.sync)
    private Button sync;

    @Nullable
    @InjectView(R.id.update)
    private Button update;

    @Nullable
    @InjectView(R.id.dashPriceList)
    private TextView dashPriceList;

    @Nullable
    @InjectView(R.id.map)
    private Button map;

    boolean updated = false;
    boolean justCreated;
    boolean actualizandoData = false;

    private Realm realm;

    /**
     * @see com.spm.android.common.activity.AbstractActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dashboard_activity);

        userRepository = new DBUserRepository(this);

        clientes.setOnClickListener(new LaunchOnClickListener(
                ClientsActivity.class));
        sync.setOnClickListener(new LaunchOnClickListener(SyncActivity.class));

        updateDataUseCase = (UpdateDataUseCase) getLastNonConfigurationInstance();
        if (updateDataUseCase == null) {
            updateDataUseCase = getInstance(UpdateDataUseCase.class);
        }
        updateDataUseCase.addListener(this);
        if (updateDataUseCase.isInProgress()) {
            onStartUseCase();
        } else if (updateDataUseCase.isFinishSuccessful()) {
            onFinishUseCase();
        } else if (updateDataUseCase.isFinishFailed()) {
            onFinishUseCase();
        }

        update.setOnClickListener(new AsyncOnClickListener() {

            @Override
            public void onAsyncRun(View view) {
                updateDataUseCase();
            }
        });

        userName.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
               // testApi();
            }
        });
        justCreated = true;
    }

    @DebugLog
    private void updateDataUseCase() {
        if (!actualizandoData) {
            actualizandoData = true;
            ToastUtils.showToastOnUIThread("Actualizando...");
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    dashPriceList.setText("Actualizando...");
                    showLoading();
                }
            });

            ThreadUtils.execute(new Runnable() {
                @Override
                public void run() {
                    APIServiceImpl apiService = new APIServiceImpl();
                    User user = Application.APPLICATION_PROVIDER.get().getUser();
                    Date dateOfPriceList = apiService.getPriceListDate();

                    List<Client> clients = apiService.getClients(user);
                    //clientRepository.addAll(clients);

                    List<Product> products = apiService.getProducts();
                    //productRepository.addAll(products); // muyy lento! 45s.

                    saveClientsAndProducts(clients, products);

                    user.setUpdateDate(dateOfPriceList);
                    // Can be changed to // priceListDate, // and use updateDate for
                    // other purpose
                    userRepository.add(user);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateDate();
                            dismissLoading();
                        }
                    });
                    actualizandoData = false;
                }
            });

        } else {
            ToastUtils.showToastOnUIThread("Actualizando, espere por favor.");
        }
    }

    @DebugLog
    private void saveClientsAndProducts(List<Client> clients, List<Product> products) {
        // Initialize Realm
        Realm.init(this);
        // Get a Realm instance for this thread
        realm = Realm.getDefaultInstance();
        // Persist your data in a transaction
        realm.beginTransaction();

        realm.copyToRealmOrUpdate(clients);
        realm.copyToRealmOrUpdate(products);

        realm.commitTransaction();
        realm.close();

        //todo!!!
//        Product entity2 = get(entity.getId());
//        if (entity2 != null) {
//            entity2.modify(entity.getName(), entity.getPriceList(), entity.getPrice1(), entity.getPrice1ant(),
//                    entity.getPrice2(), entity.getPrice2ant(), entity.getPrice3(), entity.getPrice3ant(),
//                    entity.getPrice4(), entity.getPrice4ant(), entity.getPrice5(), entity.getPrice5ant());
//            entity = entity2;
//        }
    }

    private void testApi() {
        // Initialize Realm
        Realm.init(this);
        // Get a Realm instance for this thread
        realm = Realm.getDefaultInstance();
        // Build the query looking at all users:
        RealmQuery<Client> query = realm.where(Client.class);
        // Execute the query:
        RealmResults<Client> result1 = query.findAll().sort("firstName");

        ToastUtils.showToast(result1.last().getFirstName());
    }

    /**
     * @see roboguice.activity.RoboActivity#onRetainNonConfigurationInstance()
     */
    @Override
    public Object onRetainNonConfigurationInstance() {
        return updateDataUseCase;
    }

    /**
     * @see com.spm.android.common.activity.AbstractActivity#onFinishUseCase()
     */
    @Override
    public void onFinishUseCase() {
        Log.e("DASH", "onFinishUseCase");
        try {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    updateDate();
                    updated = true;
                    // datos actualizados a la ultima version de la base
                    clientes.setBackgroundResource(R.drawable.button_flat_inverted);
                    update.setBackgroundResource(R.drawable.button_flat_inverted);

                    // dismissLoading();
                    ToastUtils.showToastOnUIThread("Datos actualizados");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinishFailedUseCase(AndroidException androidException) {
        Log.e("DASH", "onFinishUseCaseERROR");
        try {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    updateDate();
                    updated = true;
                    // datos actualizados a la ultima version de la base
                    clientes.setBackgroundResource(R.drawable.button_flat_inverted);
                    update.setBackgroundResource(R.drawable.button_flat_inverted);

                    // dismissLoading();
                    ToastUtils.showToastOnUIThread("Datos actualizados");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     * @see android.app.Activity#onBackPressed()
//     */
//    @Override
//    public void onBackPressed() {
//        AlertDialogUtils.showExitOkCancelDialog();
//    }

    /**
     * @see com.spm.android.common.activity.AbstractActivity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();

        User user = AndroidApplication.get().getUser();
        if (user == null) {
            ActivityLauncher.launchActivity(LoginActivity.class);
            finish();
        } else if (!Application.APPLICATION_PROVIDER.get().getUser()
                .checkValidDate()) {
            //user.setUpdateDate(new Date());
            //Application.APPLICATION_PROVIDER.get().attach(user);
            AndroidApplication.get().logout();
        } else {
            userName.setText("Bienvenido/a: " + user.getFirstName());
        }

        updateDate();

        checkSync();

        checkUpdatedData();

        if (justCreated) {
            // update.performClick(); // only first time...
            justCreated = false;
        }
    }

    private void updateDate() {
        try {
            User user = AndroidApplication.get().getUser();
            if (user != null) {
                if (dashPriceList == null) {
                    dashPriceList = (TextView) findViewById(R.id.dashPriceList);
                }
                if (user.getUpdateDate() == null) {
                    dashPriceList.setText(LocalizationUtils.getString(
                            R.string.priceDate, "no actualizado"));
                } else {
                    dashPriceList.setText(LocalizationUtils.getString(
                            R.string.priceDate, user.getUpdateDate()
                                    .toLocaleString()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void checkSync() {
        syncUseCase = getInstance(SyncUseCase.class);
        syncUseCase.doExecute();
        List<Work> works = syncUseCase.getWorks();
        boolean synced = true;
        for (Iterator<Work> iterator = works.iterator(); iterator.hasNext(); ) {
            Work work = iterator.next();
            if (work.getSync() == false) {
                synced = false;
            }
        }

        if (synced) {
            sync.setBackgroundResource(R.drawable.button_flat_inverted);
        } else {
            sync.setBackgroundResource(R.drawable.button_cancel);
        }
    }

    protected void checkUpdatedData() {
        updateDataUseCase = getInstance(UpdateDataUseCase.class);
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                updated = updateDataUseCase.isUpdatedData();

                // Get a handler that can be used to post to the main thread
                Handler mainHandler = new Handler(getApplicationContext()
                        .getMainLooper());
                Runnable myRunnable = new Runnable() {

                    @Override
                    public void run() {
                        if (updated) {
                            // datos actualizados a la ultima version de la base
                            clientes.setBackgroundResource(R.drawable.button_flat_inverted);
                            update.setBackgroundResource(R.drawable.button_flat_inverted);
                        } else {
                            clientes.setBackgroundResource(R.drawable.button_cancel);
                            update.setBackgroundResource(R.drawable.button_cancel);
                        }
                    }
                };
                mainHandler.post(myRunnable);
            }
        };
        ThreadUtils.execute(runnable);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // menu.add(Menu.NONE, R.drawable.ic_sync, Menu.NONE, R.string.resync)
        // .setIcon(R.drawable.ic_sync);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.drawable.ic_sync:
                update.performClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void update() {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                executeUseCase(updateDataUseCase);
            }
        };

        executeOnUIThread(new Runnable() {

            @Override
            public void run() {
                AlertDialogUtils.showOkCancelDialog(dialogClickListener,
                        R.string.confirmUpdateTitle, R.string.confirmUpdateMsg,
                        R.string.update, R.string.notUpdate);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateDataUseCase.removeListener(this);
    }
}
