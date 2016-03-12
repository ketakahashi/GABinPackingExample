import java.util.List;

import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.BinaryChromosome;
import org.apache.commons.math3.genetics.BinaryMutation;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.FixedGenerationCount;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.OnePointCrossover;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.StoppingCondition;
import org.apache.commons.math3.genetics.TournamentSelection;

public class GATest {
	
	private Integer [] weight; //各袋の重量
	private int targetWeight; //目標重量

	class SimpleChromosome extends BinaryChromosome {

		public SimpleChromosome(Integer[] representation) {
			super(representation);
		}

		public SimpleChromosome(List<Integer> representation) {
			super(representation);
		}

		@Override
		public AbstractListChromosome<Integer> newFixedLengthChromosome(List<Integer> representation) {
			return new SimpleChromosome(representation);
		}
		
		@Override
		public double fitness() {
			double fitness = 0.0;
			int idx = 0;
			List<Integer> representation = this.getRepresentation();
			for (int x : representation) {
				if (x == 1) 
					fitness += weight[idx];
				idx++;
			}
			return 1 / ((targetWeight - fitness) * (targetWeight - fitness));
		}
	}
	
	public void execute() {
		// initialize a new genetic algorithm
		GeneticAlgorithm ga = new GeneticAlgorithm(
		    new OnePointCrossover<Integer>(), //CrossoverPolicy
		    1, //crossoverRate
		    new BinaryMutation(), //MutationPolicy
		    0.10, //mutationRate
		    new TournamentSelection(2) //SelectionPolicy
		);
		
		// initial population
		Population initial = new ElitisticListPopulation(100, 0.1); //populationLimit, elitismRate
		for (int i = 0; i < 100; i++)
			initial.addChromosome(new SimpleChromosome(BinaryChromosome.randomBinaryRepresentation(10)));
		        
		// stopping condition
		StoppingCondition stopCond = new FixedGenerationCount(100);
		        
		// run the algorithm
		Population finalPopulation = ga.evolve(initial, stopCond);
		        
		// best chromosome from the final population
		Chromosome bestFinal = finalPopulation.getFittestChromosome();
		
		System.out.println(bestFinal);
	}
	
	public GATest setWeights(Integer[] weight) {
		this.weight = weight;
		return this;
	}
	
	public GATest setTargetWeight(int target) {
		this.targetWeight = target;
		return this;
	}
	
	public static void main(String [] args) {
		new GATest()
		.setWeights(new Integer[] {1,2,3,4,5,6,7,8,9,10})
		.setTargetWeight(7)
		.execute();
	}
}
