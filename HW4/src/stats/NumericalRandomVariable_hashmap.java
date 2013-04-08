package stats;

public class NumericalRandomVariable_hashmap<Type extends Number> extends
		SampledRandomVariable<Type> 
{

	@Override
	public void forget(Type val) {
		super.forget(val);
		
		X -= val.doubleValue();
		X_sq -= val.doubleValue() * val.doubleValue();
		
		//TODO:  Min/Max operations.
		if(!super.containsSample(val))
		{
			if(val.equals(min))
			{
				double minVal = Double.POSITIVE_INFINITY;
				for(Type n:this.getUniqueObservations())
				{
					if(n.doubleValue() < minVal) min = n;
				}
			}
			else if(val.equals(max))
			{
				double maxVal = Double.NEGATIVE_INFINITY;
				for(Type n:this.getUniqueObservations())
				{
					if(n.doubleValue() > maxVal) max = n;
				}
			}
		}
	}

	@Override
	public void forgetMultiple(Type val, long count) {
		// TODO Auto-generated method stub
		super.forgetMultiple(val, count);

		X -= count * val.doubleValue();
		X_sq -= count * val.doubleValue() * val.doubleValue();
		
		//TODO:  Min/Max operations.
		if(!super.containsSample(val))
		{
			if(val.equals(min))
			{
				double minVal = Double.POSITIVE_INFINITY;
				for(Type n:this.getUniqueObservations())
				{
					if(n.doubleValue() < minVal) min = n;
				}
			}
			else if(val.equals(max))
			{
				double maxVal = Double.NEGATIVE_INFINITY;
				for(Type n:this.getUniqueObservations())
				{
					if(n.doubleValue() > maxVal) max = n;
				}
			}
		}
	}

	private double X;
	private double X_sq;
	
	private Type min;
	private Type max;

	public NumericalRandomVariable_hashmap()
	{
		super();
		
		X = 0;
		X_sq = 0;
	}

	public NumericalRandomVariable_hashmap(int initialCapacity)
	{
		super(initialCapacity);
		
		X = 0;
		X_sq = 0;
	}
	
	@Override
	public void observe(Type val) 
	{
		super.observe(val);
		
		X = X + val.doubleValue();
		X_sq = X_sq + val.doubleValue() * val.doubleValue();
		
		if(min == null)
		{
			min = val;
			max = val;
		}
		
		if(val.doubleValue() < min.doubleValue()) min = val;
		if(max.doubleValue() < val.doubleValue()) max = val;
	}
	
	@Override
	public void observeMultiple(Type val, long count)
	{
		super.observeMultiple(val, count);
		
		X = X + count * val.doubleValue();
		X_sq = X_sq + count * val.doubleValue() * val.doubleValue();
		
		if(min == null)
		{
			min = val;
			max = val;
		}
		
		if(val.doubleValue() < min.doubleValue()) min = val;
		if(max.doubleValue() < val.doubleValue()) max = val;
	}
	
	//max, min, mean, variance.
	
	public final Type getMaximum()
	{
		return max;
	}
	
	public final Type getMinimum()
	{
		return min;
	}
	
	public final double getMean()
	{
		return X / this.getCount();
	}
	
	public final double getSampleVariance()
	{
		double xsq = X_sq;
		double count = this.getCount();
		double mean = X / this.getCount();
		
		return (1 / (count - 1)) * (xsq - count * mean * mean);
	}
	
	public final double getPopulationVariance()
	{
		double xsq = X_sq;
		double count = this.getCount();
		double mean = X / this.getCount();
		
		return (1 / count) * (xsq - count * mean * mean);
	}
}