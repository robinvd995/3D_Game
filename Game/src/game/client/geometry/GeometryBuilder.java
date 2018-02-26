package game.client.geometry;

public class GeometryBuilder {

	public static float[] buildCenteredCube(float size){
		return new float[]{
				-size,  size, -size,
			    -size, -size, -size,
			    size, -size, -size,
			     size, -size, -size,
			     size,  size, -size,
			    -size,  size, -size,

			    -size, -size,  size,
			    -size, -size, -size,
			    -size,  size, -size,
			    -size,  size, -size,
			    -size,  size,  size,
			    -size, -size,  size,

			     size, -size, -size,
			     size, -size,  size,
			     size,  size,  size,
			     size,  size,  size,
			     size,  size, -size,
			     size, -size, -size,

			    -size, -size,  size,
			    -size,  size,  size,
			     size,  size,  size,
			     size,  size,  size,
			     size, -size,  size,
			    -size, -size,  size,

			    -size,  size, -size,
			     size,  size, -size,
			     size,  size,  size,
			     size,  size,  size,
			    -size,  size,  size,
			    -size,  size, -size,

			    -size, -size, -size,
			    -size, -size,  size,
			     size, -size, -size,
			     size, -size, -size,
			    -size, -size,  size,
			     size, -size,  size
		};
	}
}
