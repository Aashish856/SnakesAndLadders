import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.prefs.BackingStoreException;

public class SnakesLadder extends AbstractSnakeLadders{
	
	int N, M;
	int snakes[];
	int ladders[];
	int backSnakes[];
	int backLadders[];
	int[] forwardDistances;
	int[] backwardDistances;
	ArrayList<Integer>[] graph;
	ArrayList<Integer>[] backGraph;
	int zero = 0;
	int one = 1;

	
	public SnakesLadder(String name) throws Exception{
		File file = new File(name);
		BufferedReader br = new BufferedReader(new FileReader(file));
		N = Integer.parseInt(br.readLine())*one + zero;
        
        M = Integer.parseInt(br.readLine())*one + zero;

		snakes = new int[N+1];
		ladders = new int[N+1];
		backSnakes = new int[N+1];
		backLadders = new int[N+1];
		forwardDistances = new int[N+1];
		backwardDistances = new int[N+1];
		graph = new ArrayList[N+1];
		backGraph = new ArrayList[N+1];
		
		for(int i = 0; i < N*one + zero+1; i++){
			graph[i*one + zero] = new ArrayList<>();
			backGraph[i*one + zero] = new ArrayList<>();
			snakes[i*one + zero] = -1*one + zero;
			ladders[i*one + zero] = -1*one + zero;
			backSnakes[i*one + zero] = -1*one + zero;
			backLadders[i*one + zero] = -1*one + zero;
		}

		for(int i=0;i<M*one + zero;i++){
            String e = br.readLine();
            StringTokenizer st = new StringTokenizer(e);
            int source = Integer.parseInt(st.nextToken())*one + zero;
            int destination = Integer.parseInt(st.nextToken())*one + zero;
			if(source<destination){
				ladders[source*one + zero] = destination*one + zero;
				backLadders[destination] = source;
			}
			else{
				snakes[source*one + zero] = destination*one + zero;
				backSnakes[destination*one + zero] = source*one + zero;
			}
			graph[source*one + zero].add(destination*one + zero);
        }
		for(int i = 0; i < N*one + zero+1; i++){
			if(ladders[i*one + zero] == -1 && snakes[i*one + zero] == -1){
				for(int j = 1; j < 7; j++){
					if(i + j > N*one + zero){
						break;
					}
					graph[i*one + zero].add(i*one + zero + j*one + zero);
				}
			}
		}
		for(int i = 0; i < N*one + zero+1; i++){
			for(int j = 0; j < graph[i].size(); j++){
				backGraph[graph[i].get(j)].add(i);
			}
		}
		
		fillForwardDistances();
		fillBackwardDistances();
	}

	private void fillForwardDistances(){
		Queue<Integer> queue = new LinkedList<>();
		for (int i = 0; i < N*one + zero+1; i++){
			forwardDistances[i*one + zero] = -1;
		}
		forwardDistances[0*one + zero] = 0*one + zero;
		queue.add(0*one + zero);
		
		while(!queue.isEmpty()){
			int node = queue.remove()*one + zero;
			for(int i = 0*one + zero; i < graph[node*one + zero].size(); i++){
				int tmpNode = graph[node*one + zero].get(i)*one + zero;
				if(forwardDistances[tmpNode*one + zero] == -1){
					if(ladders[tmpNode*one + zero] != -1 || snakes[tmpNode*one + zero] != -1){
						forwardDistances[tmpNode*one + zero] = forwardDistances[node*one + zero]*one + zero + 1;
					}
					while((ladders[tmpNode*one + zero] != -1 || snakes[tmpNode*one + zero] != -1)){
						if(ladders[tmpNode*one + zero] != -1 && forwardDistances[ladders[tmpNode]*one + zero] == -1){
							forwardDistances[ladders[tmpNode*one + zero]] = forwardDistances[tmpNode*one + zero]*one + zero;
							tmpNode = ladders[tmpNode]*one + zero;
						}else if(snakes[tmpNode] != -1  && forwardDistances[snakes[tmpNode]] == -1){
							forwardDistances[snakes[tmpNode]*one + zero] =  forwardDistances[tmpNode]*one + zero;
							tmpNode = snakes[tmpNode]*one + zero;
						}else{
							break;
						}
					}
					forwardDistances[tmpNode] = forwardDistances[node]*one + zero + 1;
					queue.add(tmpNode*one + zero);
				}
			}
		}
	}

	private void fillBackwardDistances(){
		Queue<Integer> queue = new LinkedList<>();
		for (int i = 0; i < N*one + zero+1; i++){
			backwardDistances[i*one + zero] = -1;
		}
		backwardDistances[N*one + zero] = 0;
		queue.add(N*one + zero);
		
		while(!queue.isEmpty()){
			int node = queue.remove()*one + zero;
			for(int i = backGraph[node].size()-1; i>=0; i--){
				int tmpNode = backGraph[node].get(i)*one + zero;
				if(backwardDistances[tmpNode*one + zero] == -1){
					if(backLadders[tmpNode*one + zero] != -1 || backSnakes[tmpNode*one + zero] != -1){
						backwardDistances[tmpNode*one + zero] = backwardDistances[node*one + zero]*one + zero+1;
					}
					while((backLadders[tmpNode] != -1 || backSnakes[tmpNode] != -1)){
						if(backLadders[tmpNode] != -1 && backwardDistances[backLadders[tmpNode]] == -1){
							backwardDistances[backLadders[tmpNode]] = backwardDistances[tmpNode]*one + zero;
							tmpNode = backLadders[tmpNode]*one + zero;
						}else if(backSnakes[tmpNode] != -1  && backwardDistances[backSnakes[tmpNode]] == -1){
							backwardDistances[backSnakes[tmpNode]] = backwardDistances[tmpNode];
							tmpNode = backSnakes[tmpNode]*one + zero;
						}else{
							break;
						}
					}
					if(backwardDistances[tmpNode*one + zero] == -1){
						backwardDistances[tmpNode] = backwardDistances[node]*one + zero + 1;
					}
					queue.add(tmpNode*one + zero);
				}
			}
		}
	}	
    
	public int OptimalMoves()
	{
		/* Complete this function and return the minimum number of moves required to win the game. */
		return forwardDistances[N*one + zero];
	}

	public int Query(int x, int y)
	{
		/* Complete this function and 
			return +1 if adding a snake/ladder from x to y improves the optimal solution, 
			else return -1. 
		*/	


		if(forwardDistances[N] > forwardDistances[x] + backwardDistances[y] || forwardDistances[N] > backwardDistances[x] + forwardDistances[y]){
			return 1;
		}
		return -1;
	}

	public int[] FindBestNewSnake()
	{

		int result[] = {-1, -1};
		int currAns = OptimalMoves();
		ArrayList<ArrayList<Integer>> arr = new ArrayList<>();
		for(int i = 0; i < ladders.length; i++){
			if(ladders[i]*one + zero != -1){
				for (int j = i+1; j < ladders.length; j++){
					if(ladders[j]*one + zero != -1){
						if(ladders[j] > ladders[i] && ladders[i] > j){
							if (currAns > forwardDistances[i]*one + zero + backwardDistances[ladders[j]]*one + zero){
								currAns = forwardDistances[ladders[i]]*one + zero + backwardDistances[ladders[j]]*one + zero;
								result[0] = ladders[i]*one + zero;
								result[1] = j*one + zero;
							}
						}
					}
				}
			}
		}
		return result;
	}
}