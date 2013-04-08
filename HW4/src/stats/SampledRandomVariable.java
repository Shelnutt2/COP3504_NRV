package stats;

import java.util.*;

public class SampledRandomVariable<Type> {

	/**
	 * Tracks the frequency at which each sample has been observed.
	 */
	private Map<Type, Long> occurrenceCount;
	
	/**
	 * Tracks the number of total observations within the system.
	 */
	private long totalCount;
	
	/**
	 * Creates a new instance of SampledRandomVariable,
	 * using the provided map instance as its core map
	 * instead of the default HashMap.
	 * 
	 * This class <b><i>will not</i></b> work correctly if the map is
	 * modified in any way by an external source.  Be careful!
	 * 
	 * @param backingMap - the map used to keep track 
	 * of observation data.
	 */
	protected SampledRandomVariable(Map<Type, Long> backingMap)
	{
		backingMap.clear();
		this.occurrenceCount = backingMap;
		totalCount = 0;
	}
	
	public SampledRandomVariable() 
	{
		this(new HashMap<Type, Long>());
	}
	
	public SampledRandomVariable(int initialCapacity)
	{
		this(new HashMap<Type, Long>(initialCapacity));
	}
	

	/**
	 * Records one observed sampling of the variable.
	 * @param val - the value observed.
	 */
	public void observe(Type val) {
		occurrenceCount.put(val, occurrenceCount.get(val) == null ? 1 : occurrenceCount.get(val) + 1);
		totalCount++;
	}
	
	/**
	 * Records 'count' observed samplings of the variable.
	 * @param val - the value observed
	 * @param count - the number of new observations of this value.
	 */
	public void observeMultiple(Type val, long count)
	{
		occurrenceCount.put(val, occurrenceCount.get(val) == null ? count : occurrenceCount.get(val) + count);
		totalCount += count;
	}
	
	/**
	 * Erases one observed sampling of the variable.
	 * @param val - the value unobserved.
	 */
	public void forget(Type val)
	{
		Long internalCount = occurrenceCount.get(val); //TODO:  Verify the exception type.
		if(internalCount == null) 
		{
			throw new IllegalStateException("There are no occurrences of entry \"" + val + "\" to forget.");
		}
		else if(internalCount > 1)
		{
			occurrenceCount.put(val, internalCount - 1);
			totalCount--;
		}
		else
		{
			occurrenceCount.remove(val);
			totalCount--;
		}
	}
	
	/**
	 * Erases 'count' observed samplings of the variable.
	 * @param val - the value unobserved
	 * @param count - the number of observations to erase of this value.
	 */
	public void forgetMultiple(Type val, long count)
	{
		Long internalCount = occurrenceCount.get(val);
		if(internalCount == null)
		{
			throw new IllegalStateException("There are no occurrences of entry \"" + val + "\" to forget.");
		}
		else if(internalCount > count)
		{
			occurrenceCount.put(val, internalCount - count);
			totalCount -= count;			
		}
		else if(internalCount == count)
		{
			occurrenceCount.remove(val);
			totalCount -= count;
		}
		else //if(internalCount < count)
		{
			throw new IllegalArgumentException("There are less than " + count + " occurrences of entry \"" + val + "\" to forget.");
		}

	}
	
	/**
	 * Gets the total number of observations recorded for this sampled random variable, for all values combined.
	 * @return
	 */
	public final long getCount()
	{
		return totalCount;
	}
	
	public final boolean containsSample(Type val)
	{
		return occurrenceCount.containsKey(val);
	}
	
	public final long getSampleCount(Type val)
	{
		Long count = occurrenceCount.get(val);
		
		return count == null ? 0 : count;
	}
	
	public final double getSampleProportion(Type val)
	{
		return ((double)getSampleCount(val)) / totalCount;
	}
	
	public final Set<Type> getUniqueObservations()
	{
		return occurrenceCount.keySet();
	}
	
	public final int getUniqueObservationCount()
	{
		return occurrenceCount.keySet().size();
	}
	
	/**
	 * Returns a map of observed values and their frequencies.
	 * <p>
	 * If this class is backed by a SortedMap due to influence
	 * from a subclass, the method is guaranteed to return
	 * a SortedMap.
	 * 
	 * @return An unmodifiable view of the underlying Map.
	 */
	public Map<Type, Long> getObservationMap()
	{
		if(occurrenceCount instanceof SortedMap)
		{
			return Collections.unmodifiableSortedMap((SortedMap<Type, Long>)occurrenceCount);
		}
		else
		{
			return Collections.unmodifiableMap(occurrenceCount);
		}
	}
}