package caesar.util.interpolate;

public interface IInterpolatable<T> {

	T interpolate(double factor, T other);
}
