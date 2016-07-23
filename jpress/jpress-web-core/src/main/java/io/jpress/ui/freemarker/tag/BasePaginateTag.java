/**
 * Copyright (c) 2015-2016, Michael Yang 杨福海 (fuhai999@gmail.com).
 *
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jpress.ui.freemarker.tag;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Page;

import io.jpress.core.render.freemarker.JTag;

public abstract class BasePaginateTag extends JTag {

	final Page<?> page;

	String previous;
	String next;
	String active;
	String disabled;
	String anchor;

	public BasePaginateTag(Page<?> page) {
		this.page = page;
	}

	@Override
	public void onRender() {

		previous = getParam("previous", "previous");
		next = getParam("next", "next");
		active = getParam("active", "active");
		disabled = getParam("disabled", "disabled");
		anchor = getParam("anchor");

		int currentPage = page.getPageNumber();
		int totalPage = page.getTotalPage();

		if ((totalPage <= 0) || (currentPage > totalPage)) {
			return;
		}

		int startPage = currentPage - 4;
		if (startPage < 1) {
			startPage = 1;
		}
		int endPage = currentPage + 4;
		if (endPage > totalPage) {
			endPage = totalPage;
		}

		if (currentPage <= 8) {
			startPage = 1;
		}

		if ((totalPage - currentPage) < 8) {
			endPage = totalPage;
		}

		List<PaginateItem> pages = new ArrayList<BasePaginateTag.PaginateItem>();
		if (currentPage == 1) {
			pages.add(new PaginateItem(previous + " " + disabled, "javascript:;", "上一页"));
		} else {
			pages.add(new PaginateItem(previous, getUrl(currentPage - 1), "上一页"));
		}

		if (currentPage > 8) {
			pages.add(new PaginateItem("", getUrl(1), "1"));
			pages.add(new PaginateItem("", getUrl(2), "2"));
			pages.add(new PaginateItem(disabled, "javascript:;", "..."));
		}

		for (int i = startPage; i <= endPage; i++) {
			if (currentPage == i) {
				pages.add(new PaginateItem(active, "javascript:;", i));
			} else {
				pages.add(new PaginateItem("", getUrl(i), i));
			}
		}

		if ((totalPage - currentPage) >= 8) {
			pages.add(new PaginateItem(disabled, "javascript:;", "..."));
			pages.add(new PaginateItem("", getUrl(totalPage - 1), totalPage - 1));
			pages.add(new PaginateItem("", getUrl(totalPage), totalPage));
		}

		if (currentPage == totalPage) {
			pages.add(new PaginateItem(next + " " + disabled, "javascript:;", "下一页"));
		} else {
			pages.add(new PaginateItem(next, getUrl(currentPage + 1), "下一页"));
		}

		setVariable("pageItems", pages);
		renderBody();
	}


	public static class PaginateItem {
		private String style;
		private String url;
		private String text;

		public PaginateItem(String style, String url, String text) {
			this.style = style;
			this.url = url;
			this.text = text;
		}

		public PaginateItem(String style, String url, int text) {
			this.style = style;
			this.url = url;
			this.text = text + "";
		}

		public String getStyle() {
			return style;
		}

		public void setStyle(String style) {
			this.style = style;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
	}
	
	protected abstract String getUrl(int pageNumber);

}
