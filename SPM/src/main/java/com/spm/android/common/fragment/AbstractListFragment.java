package com.spm.android.common.fragment;

import android.content.DialogInterface;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.spm.common.exception.AndroidException;
import com.spm.common.usecase.DefaultUseCase;

/**
 * Base {@link ListFragment}
 * 
 * @param <T>
 * @author Maxi Rosson
 */
public abstract class AbstractListFragment<T> extends ListFragment implements FragmentIf, OnItemClickListener {
	
	protected FragmentIf getFragmentIf() {
		return (FragmentIf)this.getActivity();
	}
	
	/**
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View,
	 *      int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		onListItemClick((ListView)parent, v, position, id);
	}
	
	/**
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void onListItemClick(ListView listView, View v, int position, long id) {
		int headersCount = listView.getHeaderViewsCount();
		int pos = position - headersCount;
		if ((pos >= 0) && (pos < listView.getAdapter().getCount())) {
			T t = (T)listView.getAdapter().getItem(pos);
			onListItemClick(t);
		}
	}
	
	protected void onListItemClick(T item) {
		// Do Nothing
	}
	
	/**
	 * @see com.spm.android.common.fragment.FragmentIf#findView(int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <V extends View> V findView(int id) {
		return (V)getActivity().findViewById(id);
	}
	
	/**
	 * @see com.spm.android.common.fragment.FragmentIf#inflate(int)
	 */
	@Override
	public View inflate(int resource) {
		return getFragmentIf().inflate(resource);
	}
	
	/**
	 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
	 */
	@Override
	public void onCancel(DialogInterface dialog) {
		getFragmentIf().onCancel(dialog);
	}
	
	/**
	 * @see com.spm.common.usecase.listener.DefaultUseCaseListener#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		getFragmentIf().onStartUseCase();
	}
	
	/**
	 * @see com.spm.common.usecase.listener.DefaultUseCaseListener#onFinishFailedUseCase(com.spm.common.exception.AndroidException)
	 */
	@Override
	public void onFinishFailedUseCase(AndroidException androidException) {
		getFragmentIf().onFinishFailedUseCase(androidException);
	}
	
	/**
	 * @see com.spm.common.usecase.listener.DefaultUseCaseListener#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		getFragmentIf().onFinishUseCase();
	}
	
	/**
	 * @see com.spm.android.common.fragment.FragmentIf#executeOnUIThread(java.lang.Runnable)
	 */
	@Override
	public void executeOnUIThread(Runnable runnable) {
		getFragmentIf().executeOnUIThread(runnable);
	}
	
	/**
	 * @see com.spm.android.common.fragment.FragmentIf#showLoading()
	 */
	@Override
	public void showLoading() {
		getFragmentIf().showLoading();
	}
	
	/**
	 * @see com.spm.android.common.fragment.FragmentIf#showLoading(java.lang.Boolean)
	 */
	@Override
	public void showLoading(Boolean cancelable) {
		getFragmentIf().showLoading(cancelable);
	}
	
	/**
	 * @see com.spm.android.common.fragment.FragmentIf#showLoadingOnUIThread()
	 */
	@Override
	public void showLoadingOnUIThread() {
		getFragmentIf().showLoadingOnUIThread();
	}
	
	/**
	 * @see com.spm.android.common.fragment.FragmentIf#showLoadingOnUIThread(java.lang.Boolean)
	 */
	@Override
	public void showLoadingOnUIThread(Boolean cancelable) {
		getFragmentIf().showLoadingOnUIThread(cancelable);
	}
	
	/**
	 * @see com.spm.android.common.fragment.FragmentIf#dismissLoading()
	 */
	@Override
	public void dismissLoading() {
		getFragmentIf().dismissLoading();
	}
	
	/**
	 * @see com.spm.android.common.fragment.FragmentIf#dismissLoadingOnUIThread()
	 */
	@Override
	public void dismissLoadingOnUIThread() {
		getFragmentIf().dismissLoadingOnUIThread();
	}
	
	/**
	 * @see com.spm.android.common.fragment.FragmentIf#getInstance(java.lang.Class)
	 */
	@Override
	public <I> I getInstance(Class<I> clazz) {
		return getFragmentIf().getInstance(clazz);
	}
	
	/**
	 * @see com.spm.android.common.fragment.FragmentIf#getExtra(java.lang.String)
	 */
	@Override
	public <E> E getExtra(String key) {
		return getFragmentIf().getExtra(key);
	}
	
	/**
	 * @see com.spm.android.common.fragment.FragmentIf#executeUseCase(com.spm.common.usecase.DefaultUseCase)
	 */
	@Override
	public void executeUseCase(DefaultUseCase<?> useCase) {
		getFragmentIf().executeUseCase(useCase);
	}
}
