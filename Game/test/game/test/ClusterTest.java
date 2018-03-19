package game.test;

import org.junit.Test;

import game.common.world.cluster.Cluster;

public class ClusterTest {

	@Test
	public void clusterTest(){
		Cluster cluster = new Cluster(null, 0, 0, 0);
		cluster.toByteArray();
	}
}
