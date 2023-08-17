package com.samyx.util;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;

import com.samyx.util.PageItem;

public class PageRender<T> {
	private String url;

	private Page<T> page;

	private int totalPaginas;

	private int numElementosPorPagina;

	private int paginaActual;

	private Long totalElements;

	private List<PageItem> paginas;

	public PageRender(String url, Page<T> page) {
		int desde, hasta;
		this.url = url;
		this.page = page;
		this.paginas = new ArrayList<>();
		this.totalElements = Long.valueOf(page.getTotalElements());
		int showPaginas = 10;
		this.numElementosPorPagina = page.getSize();
		this.totalPaginas = page.getTotalPages();
		this.paginaActual = page.getNumber() + 1;
		if (this.totalPaginas <= showPaginas) {
			desde = 1;
			hasta = this.totalPaginas;
		} else if (this.paginaActual <= showPaginas / 2) {
			desde = 1;
			hasta = showPaginas;
		} else if (this.paginaActual >= this.totalPaginas - showPaginas / 2) {
			desde = this.totalPaginas - showPaginas + 1;
			hasta = showPaginas;
		} else {
			desde = this.paginaActual - showPaginas / 2;
			hasta = showPaginas;
		}
		for (int i = 0; i < hasta; i++)
			this.paginas.add(new PageItem(desde + i, (this.paginaActual == desde + i)));
	}

	public Long getTotalElements() {
		return this.totalElements;
	}

	public String getUrl() {
		return this.url;
	}

	public int getTotalPaginas() {
		return this.totalPaginas;
	}

	public int getPaginaActual() {
		return this.paginaActual;
	}

	public List<PageItem> getPaginas() {
		return this.paginas;
	}

	public boolean isFirst() {
		return this.page.isFirst();
	}

	public boolean isLast() {
		return this.page.isLast();
	}

	public boolean isHasNext() {
		return this.page.hasNext();
	}

	public boolean isHasPrevious() {
		return this.page.hasPrevious();
	}
}
