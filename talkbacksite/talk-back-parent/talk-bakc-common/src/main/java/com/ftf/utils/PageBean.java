package com.ftf.utils; 
 import java.util.ArrayList;
 import java.util.List;

 
 public class PageBean<T>
 {
   public static final String PAGING_BEAN = "_paging_bean";
   public static Integer DEFAULT_PAGE_SIZE = Integer.valueOf(10);
   public static final int SHOW_PAGES = 6;
   public Integer start=Integer.valueOf(0);
			public Integer pageNo=1;
   private Integer pageSize=DEFAULT_PAGE_SIZE;
   private Long totalItems = 0L;
   private List<T> datas = new ArrayList<T>();
 
   public PageBean(int start, int pageSize)
   {
     this.pageSize = Integer.valueOf(pageSize);
     this.start = Integer.valueOf(start);
   }
   public PageBean() {
     this.pageSize = DEFAULT_PAGE_SIZE;
     this.start = Integer.valueOf(0);
   }
   public PageBean(String pageNos,String pageSizes,String start) {
		if(StringUtils.isNotNull(pageSizes)){
			this.pageSize=Integer.parseInt(pageSizes);
		}else{
			this.pageSize = DEFAULT_PAGE_SIZE;
		}
		if(StringUtils.isNotNull(start)){
			this.start=Integer.parseInt(start);
			this.pageNo=this.start.intValue() / this.pageSize.intValue() + 1;
		}
		if(StringUtils.isNotNull(pageNos)){
			this.pageNo=Integer.parseInt(pageNos);
			this.start=(Integer.parseInt(pageNos)-1)*this.pageSize;
		}
   }
 
   public Integer getPageSize() {
     return this.pageSize;
   }
 
   public void setPageSize(int pageSize)
   {
     this.pageSize = Integer.valueOf(pageSize);
   }
 
   public Integer getStart()
   {
     return this.start;
   }
 
   public void setStart(Integer start)
   {
     this.start = start;
   }
 
   public void setTotalItems(Long totalItems)
   {
	   this.totalItems = totalItems;
				if(this.start>=this.totalItems){
					this.start=(int) ((getTotalPageCount()-1)*this.pageSize);
					this.pageNo=(int) getTotalPageCount();
				}
   }
 
   public int getFirstResult()
   {
     return this.start.intValue();
   }
 
   public int getCurrentPageNo()
   {
     return this.start.intValue() / this.pageSize.intValue() + 1;
   }
			/**
			 * 与getCurrentPageNo()其实结果一样。所以2个方法皆可。
			 * @return
			 */
			public int getPageNo(){
				return this.pageNo;
			}
 
   public long getTotalPageCount()
   {
     if (this.totalItems.intValue() == 0) {
       return 1L;
     }
     if (this.totalItems.intValue() % this.pageSize.intValue() == 0) {
       return this.totalItems.intValue() / this.pageSize.intValue();
     }
     return this.totalItems.intValue() / this.pageSize.intValue() + 1;
   }
 
   public Long getTotalItems()
   {
     return this.totalItems;
   }
 
   public boolean hasNextPage()
   {
     return getCurrentPageNo() < getTotalPageCount() - 1L;
   }
 
   public boolean hasPreviousPage()
   {
     return getCurrentPageNo() > 1;
   }
 
   public int getStartOfPage(int pageNo)
   {
     return getStartOfPage(pageNo, DEFAULT_PAGE_SIZE.intValue());
   }
 
   public int getStartOfPage(int pageNo, int pageSize)
   {
     if (pageNo < 1) {
       pageNo = 1;
     }
     return (pageNo - 1) * pageSize;
   }
 
   public List<T> getDatas() {
     return this.datas;
   }
 
   public void setDatas(List<T> datas) {
     this.datas = datas;
   }
 }

