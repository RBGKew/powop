package org.powo.job.delete;

class ResourceIsNotDeletableException extends Exception {
	   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceIsNotDeletableException(String msg){
	      super(msg);
	   }
	}