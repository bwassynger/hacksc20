package hacksc20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UserDB {
    Connection conn = null;
    Statement st = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    boolean isConnected = false;

    public UserDB() {
    	System.out.println("userdb constructor");
        connect();
    }
    
    public boolean isFavorite(String bookID, String userName) {
    	if(!isConnected) {
    		 connect();
    	}
    	try {
    		ps = conn.prepareStatement("SELECT * FROM Users WHERE username=?");
    		ps.setString(1, userName);
    		rs = ps.executeQuery();
    		rs.next();
    		int uID = rs.getInt("userID");
    		rs = st.executeQuery("SELECT * FROM Favorites WHERE userID=" + uID);
    		while(rs.next()) {
    			if( rs.getString("bookID").equals(bookID) ) {
    				return true;
    			}
    		}
    		return false;
    		
    	} catch (SQLException e) {
    		System.out.println("SQLException: " + e.getMessage());
    		return false;
    	}
    }
    
    public int addFavorite(String bookID, String userName) {
    	try {
    		ps = conn.prepareStatement("SELECT * FROM Users WHERE username=?");
    		ps.setString(1, userName);
    		rs = ps.executeQuery();
    		rs.next();
    		int uID = rs.getInt("userID");
    		ps = conn.prepareStatement("SELECT * FROM Favorites WHERE bookID=?");
    		ps.setString(1, bookID);
    		rs = ps.executeQuery();
    		if( rs.isBeforeFirst() ) {
    			System.out.println("Book already in favorites!");
    			return -1;
    		}
    		ps = conn.prepareStatement("INSERT INTO Favorites (userID, bookID) VALUES (?, ?)");
    		ps.setInt(1, uID);
    		ps.setString(2, bookID);
    		ps.executeUpdate();
    		return 0;
    	} catch (SQLException e) {
    		System.out.println("SQLException: " + e.getMessage());
    		return 1;
    	}
    }
    
    public int delFavorite(String bookID, String userName) {
    	try {
    		ps = conn.prepareStatement("SELECT * FROM Users WHERE username=?");
    		ps.setString(1, userName);
    		rs = ps.executeQuery();
    		rs.next();
    		int uID = rs.getInt("userID");
    		ps = conn.prepareStatement("SELECT * FROM Favorites WHERE bookID=? AND userID=?");
    		ps.setString(1, bookID);
    		ps.setInt(2, uID);
    		rs = ps.executeQuery();
    		if( !rs.isBeforeFirst() ) {
    			System.out.println("Book doesn't exist in favorites!");
    			return 0;
    		}
    		rs.next();
    		int fID = rs.getInt("fID");
    		ps = conn.prepareStatement("DELETE FROM Favorites WHERE fID=?");
    		ps.setInt(1, fID);
    		ps.executeUpdate();
    		return 0;
    	} catch (SQLException e) {
    		System.out.println("SQLException: " + e.getMessage());
    		return 1;
    	}
    }
    
    public List<String> getFavorites(String userName) {
    	try {
    		ps = conn.prepareStatement("SELECT * FROM Users WHERE username=?");
    		ps.setString(1, userName);
    		rs = ps.executeQuery();
    		rs.next();
    		int uID = rs.getInt("userID");
    		ps = conn.prepareStatement("SELECT * FROM Favorites WHERE userID=? ORDER BY time ASC");
    		ps.setInt(1, uID);
    		rs = ps.executeQuery();
    		if( !rs.isBeforeFirst() ) {
    			// No favorites exist
    			return null;
    		}
    		List<String> books = new ArrayList<String>();
    		while(rs.next()) {
    			String bookID = rs.getString("bookID");
    			books.add(bookID);
    		}
    		return books;
    	} catch (SQLException e) {
    		System.out.println("SQLException: " + e.getMessage());
    		return null;
    	}
    }
    
    // Returns error codes:
    // -1 if user not found
    // -2 if password is incorrect
    // 0 if correct info
    public int login(String userName, String userPass) {
    	if(!isConnected) {
    		connect();
    	}
    	try {
	    	ps = conn.prepareStatement("SELECT * FROM users WHERE username=?");
	    	ps.setString(1, userName);
	    	rs = ps.executeQuery();
	    	if( !rs.isBeforeFirst() ) {
	    		System.out.println("user: " + userName);
	    		System.out.println("No user found!!");
	    		return -1;
	    	}
	    	rs.next();
	    	if( rs.getString("password").equals(userPass) ) {
	    		System.out.println("Correct info");
	    		return 0;
	    	} else {
	    		System.out.println("Wrong pass!!");
	    		return -2;
	    	}
    	} catch (SQLException e) {
    		System.out.println("SQLException: " + e.getMessage());
    		return 1;
    	}
    }
    
    public int register(String userName, String userPass, int zip) {
    	if(!isConnected) {
    		connect();
    	}
    	try {
    		ps = conn.prepareStatement("SELECT * FROM users WHERE username=?");
    		ps.setString(1, userName);
    		rs = ps.executeQuery();
    		if( rs.isBeforeFirst() ) {
    			System.out.println("User already exists!");
    			return -1;
    		}
    		ps = conn.prepareStatement("INSERT INTO users (username, password, zipcode) VALUES (?, ?, ?)");
    		ps.setString(1, userName);
    		ps.setString(2, userPass);
    		ps.setInt(3, zip);
    		ps.execute();
    		return 0;
    		
    	} catch (SQLException e) {
    		System.out.println("SQLException: " + e.getMessage());
    		return 1;
    	}
    }
    
    public int log(String userName, int score) {
    	if(!isConnected) {
    		connect();
    	}
    	try {
    		ps = conn.prepareStatement("SELECT * FROM users WHERE username=?");
    		ps.setString(1, userName);
    		rs = ps.executeQuery();
    		if( !rs.isBeforeFirst() ) {
    			System.out.println("User doesn't exist!!");
    			return 1;
    		}
    		rs.next();
    		int uID = rs.getInt("userID");
    		int zip = rs.getInt("zipcode");
    		int stateID = this.findStateID(zip);
    		
    		ps = conn.prepareStatement("INSERT INTO results (zipcode, stateID, score, userID) VALUES (?, ?, ?, ?)");
    		ps.setInt(1, zip);
    		ps.setInt(2, stateID);
    		ps.setInt(3, score);
    		ps.setInt(4, uID);
    		ps.execute();
    		return 0;
    	} catch (SQLException e) {
    		System.out.println("SQLException: " + e.getMessage());
    		return 1;
    	}
    }
    
    public int logGuest(int score, int zip) {
    	if(!isConnected) {
    		connect();
    	}
    	try {
    		int stateID = this.findStateID(zip);
    		ps = conn.prepareStatement("INSERT INTO results (zipcode, stateID, score) VALUES (?, ?, ?)");
    		ps.setInt(1, zip);
    		ps.setInt(2, stateID);
    		ps.setInt(3, score);
    		ps.execute();
    		return 0;
    	} catch (SQLException e) {
    		System.out.println("SQLException: " + e.getMessage());
    		return 1;
    	}
    }
    
    public int findStateID(int zip) {
    	System.out.println("Finding state id for zip: " + zip);
    	URL apiUrl = null;
		HttpURLConnection connection = null;
		String resultString = "";
		
		String apiStart = "https://maps.googleapis.com/maps/api/geocode/json?address="+zip;
		apiStart += "&key=AIzaSyClkaCWbp6hVLfO_RKqo4FyNnOB0gejSog";

		System.out.println(apiStart);
					
		// GET URL result
		try {
			apiUrl = new URL(apiStart);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			connection = (HttpURLConnection)apiUrl.openConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			connection.setRequestMethod("GET");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line;
		
		try {
			while ((line = br.readLine()) != null) {
				resultString += line;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		@SuppressWarnings("deprecation")
		JsonParser jsonParser = new JsonParser();
		@SuppressWarnings("deprecation")
		JsonArray resultJsonArray = jsonParser.parse(resultString).getAsJsonObject().getAsJsonArray("results");
		JsonObject innerResult = resultJsonArray.get(0).getAsJsonObject();
		JsonArray components = innerResult.get("address_components").getAsJsonArray();
		String state = components.get(2).getAsJsonObject().get("long_name").getAsString();
		
		System.out.println(state);
		try {
		ps = conn.prepareStatement("SELECT * FROM states WHERE name=?");
		ps.setString(1, state);
		rs = ps.executeQuery();
		if( !rs.isBeforeFirst() ) {
			//state not in table, add
			ps = conn.prepareStatement("INSERT INTO states(name) VALUES (?)");
			ps.setString(1, state);
			ps.execute();
			ps = conn.prepareStatement("SELECT * FROM states WHERE name=?");
			ps.setString(1, state);
			rs = ps.executeQuery();
			rs.next();
			int stateID = rs.getInt("stateID");
			return stateID;
		} else {
			rs.next();
			int stateID = rs.getInt("stateID");
			return stateID;
		}
		} catch (Exception e) {
			System.out.println("SQL fuckup: " + e.getMessage());
		}
		
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
    }
    
    public List<ArrayList<Object>> getStats() {
    	if(!isConnected) {
    		connect();
    	}
    	try {
    		List<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
    		int[] nState = new int[50];
    		int[] stateTotal = new int[50];
    		rs = st.executeQuery("SELECT * FROM results");
    		System.out.println("looping through result table");
    		while(rs.next()) {
    			// loop through all results
    			int sID = rs.getInt("stateID");
    			int score = rs.getInt("score");
    			nState[sID]++;
    			stateTotal[sID] += score;
    		}
    		for(int i=0; i<50; i++) {
    			if(nState[i] != 0) {
    				ArrayList<Object> object = new ArrayList<Object>();
    				System.out.println(i + " test " + nState[i]);
    				ps = conn.prepareStatement("SELECT * FROM states WHERE stateID=?");
    				ps.setInt(1, i);
    				rs = ps.executeQuery();
    				rs.next();
    				String state = rs.getString("name");
    				int average = stateTotal[i]/nState[i];
    				object.add(state);
    				object.add(average);
    				object.add(nState[i]);
    				result.add(object);
    			}
    		}
    		return result;
    	} catch (Exception e) {
    		System.out.println("EXCEPTION: " + e.getMessage());
    		return null;
    	}
    	
    }

    public void increment(String page, String addr, int port) {
        System.out.println("FUCK");
        if(!isConnected) {
            connect();
        }
        try {

            ps = conn.prepareStatement("SELECT * FROM Page WHERE pageName=?");
            ps.setString(1, page);
            rs = ps.executeQuery();
            rs.beforeFirst();
            rs.next();
            int id = rs.getInt("pageID");
            ps = conn.prepareStatement("SELECT * FROM PageVisited WHERE pageID=?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            boolean found = false;
            while(rs.next()) {
                System.out.println(rs.getString("IPAddress"));
                System.out.println(rs.getInt("portNum"));
                if(rs.getString("IPAddress").equalsIgnoreCase(addr) && rs.getInt("portNum") == port) {

                    int pvid = rs.getInt("pageVisitedID");
                    int count = rs.getInt("count");
                    count = count+1;
                    ps = conn.prepareStatement("UPDATE PageVisited SET count=? WHERE pageVisitedID=?");
                    ps.setInt(1, count);
                    ps.setInt(2, pvid);
                    ps.executeUpdate();
                    found = true;
                }
            }
            if(!found) {
                System.out.println("socket not found");
                ps = conn.prepareStatement("INSERT INTO PageVisited (pageID, IPAddress, portNum, count) " +
                        "VALUES (?, ?, ?, ?)");
                ps.setInt(1, id);
                ps.setString(2, addr);
                ps.setInt(3, port);
                ps.setInt(4, 1);
                ps.executeUpdate();
            }

            //ps = conn.prepareStatement("SELECT * FROM PageVisited WHERE
            /*
            String name = "Sheldon";
            // rs = st.executeQuery("SELECT * from Student where fname='" + name + "'");
            ps = conn.prepareStatement("SELECT * FROM Student WHERE fname=?");
            ps.setString(1, name); // set first variable in prepared statement
            rs = ps.executeQuery();
            while (rs.next()) {
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                int studentID = rs.getInt("studentID");
                System.out.println ("fname = " + fname);
                System.out.println ("lname = " + lname);
                System.out.println ("studentID = " + studentID);
            }

             */

        } catch (SQLException e) {
            System.out.println("SQLExcept: " + e.getMessage());
        }
        //close();
    }

    public void connect() {
        System.out.println("Connecting");
        try {
            conn = DriverManager.getConnection("jdbc:mysql://google/Ceres?cloudSqlInstance=hacksc-266922:us-central1:hacksc-db" +
                    "&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false&user=bwassynger" +
                    "&password=illuminati2");
            st = conn.createStatement();
            /*
            String name = "Sheldon";
            // rs = st.executeQuery("SELECT * from Student where fname='" + name + "'");
            ps = conn.prepareStatement("SELECT * FROM Student WHERE fname=?");
            ps.setString(1, name); // set first variable in prepared statement
            rs = ps.executeQuery();
            while (rs.next()) {
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                int studentID = rs.getInt("studentID");
                System.out.println ("fname = " + fname);
                System.out.println ("lname = " + lname);
                System.out.println ("studentID = " + studentID);
            }

             */
            System.out.println(conn.toString());
            System.out.println(st.toString());
            isConnected = true;
        } catch (SQLException sqle) {
            System.out.println ("SQLException: " + sqle.getMessage());
        }
    }

    public void close() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException sqle) {
            System.out.println("sqle: " + sqle.getMessage());
        }
    }

    public boolean isConnected() {
        return !(conn == null);
    }

}