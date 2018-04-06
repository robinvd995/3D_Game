package caesar.util.interpolate;

import caesar.util.MathHelper;

public class InterpolateFunctions {

	public static final IInterpolateFunction FUNCTION_LINEAR = new IInterpolateFunction(){

		@Override
		public double getFactor(double time, double startTime, double endTime) {
			return (time - startTime) / (endTime - startTime);
		}
		
	};
	
	public static final IInterpolateFunction FUNCTION_SMOOTHSTEP = new IInterpolateFunction(){

		@Override
		public double getFactor(double time, double startTime, double endTime) {
			double x = MathHelper.clamp((time - startTime) / (endTime - startTime));
			return x * x * (3 - 2 * x);
		}
		
	};
}
