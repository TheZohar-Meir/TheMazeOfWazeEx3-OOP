package gameClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static gameClient.SimpleDB.*;

class DB_Reader {

	private static List<Integer> LevelCase = new ArrayList<Integer>();


	public DB_Reader(){
		LevelCase.add(0);
		LevelCase.add(1);
		LevelCase.add(3);
		LevelCase.add(5);
		LevelCase.add(9);
		LevelCase.add(11);
		LevelCase.add(13);
		LevelCase.add(16);
		LevelCase.add(19);
		LevelCase.add(20);
		LevelCase.add(23);
	}

	static String PrintDataFromServer(int id) {

		StringBuilder str = new StringBuilder();

		try {

			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			ResultSet resultSet = null;
			int PlayGameCounter = 0;
			int MaxLevel = 0;
			str.append("Max Score per level Played by: ").append(id).append("\n").append("\n");
			
			for (int i = 0; i <= 23 ; i++) { // loop that checks each level how many times i had played. 
				
				boolean Do_i_have_a_score = false;
				int MaxScore = 0;
				int MinimumMov = Integer.MAX_VALUE;
				String allCustomersQuery = "SELECT * FROM Logs where userID="+id+" AND levelID="+i;
				resultSet = statement.executeQuery(allCustomersQuery);
				str.append("Level ").append(i).append(") ");
				Timestamp time = null;
				
				while (resultSet.next())  //counting hoe many times i played at this level.
				{
					Do_i_have_a_score = true;
					PlayGameCounter++;
					int level = resultSet.getInt("levelID");
					if (level > MaxLevel) {MaxLevel = level;}
					
					boolean Toughlevel = allUsers(i);
					int MyScore = resultSet.getInt("score");
					int MovesNum = resultSet.getInt("moves");
					
					if (Toughlevel) {
						if (underMaxMoves(MovesNum, level)) {
							if (MyScore > MaxScore) {
								MaxScore = MyScore;
								MinimumMov = MovesNum;
								time = resultSet.getTimestamp("time");
							}
						}
					} 
					else 
					{
						if (MyScore > MaxScore) 
						{
							MaxScore = MyScore;
							MinimumMov = MovesNum;
							time = resultSet.getTimestamp("time");
						}
					}
				}
				if(Do_i_have_a_score && time != null)
				{
					str.append("Best score: ").append(MaxScore).append(", Minimum calls for server: ").append(MinimumMov).append(", at Local Time: ").append(time.toString()).append("\n");
				}
				else
				{
					str.append("Not played / passed the requirements").append("\n");
				}
			}
			str.append("\n").append("ID number: ").append(id).append(" has Played total ").append(PlayGameCounter).append(" games.").append("\n").append("MaxLevel Reached is:").append(MaxLevel).append("\n");
			resultSet.close();
			statement.close();
			connection.close();
		}
		catch (SQLException SQLE) {System.out.println("SQLException: " + SQLE.getMessage());}
		catch (ClassNotFoundException error) {error.printStackTrace();}
		return str.toString();
	}

	static String UsersAttempts(int Id_number){

		StringBuilder str = new StringBuilder();
		str.append("Number of attempts by players to passe each stage of the game\r\n" + 
				":").append("\n");
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			ResultSet resultSet = null;
			for(int level:LevelCase) 
			{
				String allCustomersQuery = "SELECT * FROM Logs "+level;
				resultSet = statement.executeQuery(allCustomersQuery);
				int ind = 0;
				boolean OtherPlayer = true;
				while ( resultSet.next() && OtherPlayer ) 
				{
					ind++;
					if(resultSet.getInt("userID") == Id_number){
						OtherPlayer = false;
					}
				}
				str.append(level).append(") ").append(ind).append("\n");
			}
			assert resultSet != null;
			resultSet.close();
			statement.close();
			connection.close();
		}
		catch (SQLException SQLE) {System.out.println("SQLException: " + SQLE.getMessage());}
		catch (ClassNotFoundException e) {e.printStackTrace();}
		return str.toString();
	}


	private static boolean underMaxMoves(int moves,int level){
		switch(level) {
		case 0 :
			return moves <= 290;
		case 1:
			return moves <= 580;
		case 3:
			return moves <= 580;
		case 5:
			return moves <= 500;
		case 9:
			return moves <= 580;
		case 11:
			return moves <= 580;
		case 13:
			return moves <= 580;
		case 16:
			return moves <= 290;
		case 19:
			return moves <= 580;
		case 20:
			return moves <= 290;
		case 23:
			return moves <= 1140;

		default: return false;
		}
	}

	// return if i have passed the specific level.
	static boolean allUsers(int level){
		return LevelCase.contains(level);
	}


}