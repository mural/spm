package com.spm.android.activity;

import java.util.List;
import com.google.inject.Inject;
import com.google.inject.internal.Lists;
import com.spm.common.usecase.DefaultAbstractUseCase;
import com.spm.domain.Line;
import com.spm.repository.LineRepository;
import com.spm.service.APIService;

/**
 * Use case to handle the splatt screen.
 * 
 * @author Agustin Sgarlata
 */
public class LinesUseCase extends DefaultAbstractUseCase {
	
	private List<Line> lines = Lists.newArrayList();
	
	private LineRepository lineRepository;
	
	@Inject
	public LinesUseCase(APIService apiService, LineRepository lineRepository) {
		super(apiService);
		this.lineRepository = lineRepository;
	}
	
	/**
	 * @see com.splatt.common.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		lines.addAll(lineRepository.getAll());
	}
	
	/**
	 * @return the lines
	 */
	public List<Line> getLines() {
		return lines;
	}
}