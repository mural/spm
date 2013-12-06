package com.spm.android.common.listener;

import android.view.View;
import android.view.View.OnClickListener;
import com.spm.common.usecase.DefaultUseCase;
import com.spm.common.utils.ThreadUtils;

/**
 * {@link OnClickListener} that execute a {@link DefaultUseCase}
 * 
 * @author Maxi Rosson
 */
public class UseCaseOnClickListener implements OnClickListener {
	
	private DefaultUseCase<?> defaultUseCase;
	
	/**
	 * @param defaultUseCase The {@link DefaultUseCase} to execute
	 */
	public UseCaseOnClickListener(DefaultUseCase<?> defaultUseCase) {
		this.defaultUseCase = defaultUseCase;
	}
	
	/**
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public final void onClick(final View view) {
		ThreadUtils.execute(defaultUseCase);
	}
	
}
