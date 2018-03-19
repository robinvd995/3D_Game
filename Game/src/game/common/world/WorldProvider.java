package game.common.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import caesar.util.Vector3f;
import game.common.block.Block;
import game.common.world.cluster.Cluster;
import game.common.world.cluster.ClusterColumn;

public abstract class WorldProvider {

	protected Random rand = new Random();
	
	//World metadata
	public abstract String getWorldName();
	public abstract int getWorldHeight();
	
	//Time
	public abstract double getDayLength();
	
	//Client light
	public abstract Vector3f getLightColor(double time);
	public abstract Vector3f getLightDirection(double time);
	public abstract float getAmbientStrenght();
	
	//Generator
	public Cluster getCluster(World world, int clusterX, int clusterY, int clusterZ) {
		
		if(clusterY < 0 || clusterY >= getWorldHeight()){
			return null;
		}
		
		String filePath = "world/" + getWorldName() + "/data_" + clusterX + "_" + clusterY + "_" + clusterZ + ".bin";
		File file = new File(filePath);
		if(!file.exists()){
			ClusterColumn column = generateClusterColumn(world, clusterX, clusterZ);
			for(int i = 0; i < column.getHeight(); i++){
				Cluster cluster = column.getCluster(i);
				try {
					writeClusterToFile(cluster);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			return column.getCluster(clusterY);
		}
		else{
			try {
				Cluster cluster = readClusterFromFile(file, world, clusterX, clusterY, clusterZ);
				return cluster;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	private Cluster readClusterFromFile(File file, World world, int clusterX, int clusterY, int clusterZ) throws IOException{
		FileInputStream fileIn = new FileInputStream(file);
		int cSize = Cluster.CLUSTER_SIZE;
		byte[] byteArray = new byte[cSize * cSize * cSize];
		fileIn.read(byteArray);
		Cluster cluster = new Cluster(world, clusterX, clusterY, clusterZ);
		for(int i = 0; i < byteArray.length; i++){
			int x = (i / (cSize * cSize)) % cSize;
			int y = (i / (cSize)) % cSize;
			int z = i % cSize;
			int block = byteArray[i];
			System.out.println(x + "," + y + "," + z + "," +block);
			cluster.setBlock(Block.getBlockFromId(block), x, y, z);
		}
		fileIn.close();
		return cluster;
	}
	
	private void writeClusterToFile(Cluster cluster) throws IOException{
		String clusterFilePath = "world/" + getWorldName() + "/data_" + cluster.getPosition().getPosX() + "_" + cluster.getPosition().getPosY() + "_" + cluster.getPosition().getPosZ() + ".bin";
		File clusterFile = new File(clusterFilePath);
		if(!clusterFile.exists()){
			clusterFile.createNewFile();
		}
		FileOutputStream stream = new FileOutputStream(clusterFile);
		byte[] byteArray = cluster.toByteArray();
		stream.write(byteArray, 0, byteArray.length);
		stream.flush();
		stream.close();
	}
	
	protected abstract ClusterColumn generateClusterColumn(World world, int clusterX, int clusterZ);
}
