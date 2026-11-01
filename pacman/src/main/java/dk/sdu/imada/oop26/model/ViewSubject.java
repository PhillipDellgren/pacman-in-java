package dk.sdu.imada.oop26.model;

public interface ViewSubject {

    
    		/**
	 * registers the given observer at this {@link Subject}
	 * @param observer observer to be added
	 * @return if the observer could be registered
	 */
	boolean registerViewObserver(final ViewObserver observer);
    
    /**
	 * removes a given observer from this {@link Subject}
	 * @param observer	observer to be removed
	 * @return if the observer could be removed
	 */
	boolean deregisterViewObserver(final ViewObserver observer);
	
	/**
	 * informs all observers of this subject
	 */
	void notifyViewObservers();


}
