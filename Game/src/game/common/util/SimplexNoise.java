package game.common.util;

public class SimplexNoise {

	private SimplexNoiseOctave[] theOctaves;
	private double[] frequencys;
	private double[] amplitudes;
	
	public SimplexNoise(int octaves, double persistance, int seed){
		theOctaves = new SimplexNoiseOctave[octaves];
		frequencys = new double[octaves];
		amplitudes = new double[octaves];
		
		for(int i = 0; i < octaves; i++){
			theOctaves[i] = new SimplexNoiseOctave(seed);
			frequencys[i] = Math.pow(2, i);
			amplitudes[i] = Math.pow(persistance, octaves - i);
		}
	}
	
	public double getNoise(int x, int y, int z){
		double result = 0.0D;
		for(int i = 0; i < theOctaves.length; i++){
			double freq = frequencys[i];
			double amp = amplitudes[i];
			result += theOctaves[i].noise(x / freq, y / freq, z / freq);
		}
		return result;
	}
	
	/*SimplexNoiseOctave[] octaves;
    double[] frequencys;
    double[] amplitudes;

    int largestFeature;
    double persistence;
    int seed;

    public SimplexNoise(int largestFeature,double persistence, int seed){
        this.largestFeature=largestFeature;
        this.persistence=persistence;
        this.seed=seed;

        //recieves a number (eg 128) and calculates what power of 2 it is (eg 2^7)
        int numberOfOctaves=(int)Math.ceil(Math.log10(largestFeature)/Math.log10(2));

        octaves=new SimplexNoiseOctave[numberOfOctaves];
        frequencys=new double[numberOfOctaves];
        amplitudes=new double[numberOfOctaves];

        Random rnd=new Random(seed);

        for(int i=0;i<numberOfOctaves;i++){
            octaves[i]=new SimplexNoiseOctave(rnd.nextInt());

            frequencys[i] = Math.pow(2,i);
            amplitudes[i] = Math.pow(persistence,octaves.length-i);




        }

    }

    public double getNoise(int x,int y, int z){

        double result=0;

        for(int i=0;i<octaves.length;i++){
          double frequency = Math.pow(2,i);
          double amplitude = Math.pow(persistence,octaves.length-i);

          result=result+octaves[i].noise(x/frequency, y/frequency,z/frequency)* amplitude;
        }


        return result;

    }*/
}
