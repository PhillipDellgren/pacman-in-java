package dk.sdu.imada.oop26.model;


public interface Subject {

	/**
	 * registers the given observer at this {@link Subject}
	 * @param observer observer to be added
	 * @return if the observer could be registered
	 */
	boolean registerObserver(final GameObserver observer);

	/**
	 * removes a given observer from this {@link Subject}
	 * @param observer	observer to be removed
	 * @return if the observer could be removed
	 */
	boolean deregisterObserver(final GameObserver observer);
	
	/**
	 * informs all observers of this subject
	 */
	void notifyObservers();




}
