package org.example;
import java.sql.*;
import java.text.DecimalFormat;

// ASSIGNMENT
/*
CREATE A DATABASE OF MOVIE, DIRECTOR, ACTOR, (MOVIE_ACTOR)
* WRITE A STORED PROCEDURE TO CHECK HOW MANY MOVIES AN ACTOR APPEARS IN
* WRITE A STORED PROCEDURE TO GEL ALL MOVIES BY THE ACTOR
* WRITE A STORED PROCEDURE TO GET HOW MANY MOVIES A DIRECTOR HAS DIRECTED
* WRITE A STORED PROCEDURE TO GET HOW MANY MOVIES THE ACTOR HAS ACTED DIRECTED BY A DIRECTOR
* WRITE A STORED PROCEDURE TO CALCULATE AND RETURN HOW MUCH MONEY A DIRECTOR MAKES IN A SPECIFIC TIME
*/

public class Main {

    public static void main(String[] args) {

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/java_class_db", "root", "admin");

             System.out.println("\n");
             getAllMoviesByActorCount(conn, "Tom");
             System.out.println("--------------");
             getAllMoviesByActor(conn, "Tom"); // Emma
             System.out.println("--------------");
             getAllMoviesCountByDirector(conn, "Steven");
             System.out.println("--------------");
             getActorMovieAppCountByMovieDirector(conn, "Tom", "Steven");
             System.out.println("--------------");
             getDirectorProfit(conn, "Steven", "1993-07-09", "1993-08-09");


        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    private static void getAllMoviesByActorCount(Connection connection, String actorName) throws SQLException {
        PreparedStatement pStmt = connection.prepareStatement("{call getActorMovieAppearances(?)}");
        pStmt.setString(1, actorName);

        ResultSet resultSet = pStmt.executeQuery();

        while (resultSet.next()) {
            String name = resultSet.getString(1);
            int moviesCount = resultSet.getInt(2);
            if(name != null && moviesCount !=0) {
                System.out.println("Actor " + name + " has acted in " + moviesCount + " movie.");
            }else{
            System.out.println("About actor " + actorName + " we don't have any information!");
            }
        }
    }



    public static void getAllMoviesByActor(Connection connection, String actorName) throws SQLException {
        PreparedStatement pStmt = connection.prepareStatement("{call getAllMoviesByActor(?)}");
        pStmt.setString(1, actorName);

        ResultSet resultSet = pStmt.executeQuery();


        if (!resultSet.next()) {
            System.out.println("About actor " + actorName +" we don't have any information!");
        } else {
            do {
                String name = resultSet.getString(1);
                String movie = resultSet.getString(2);
                if (name != null) {
                    System.out.println("Actor " + name + " has acted in the movie: " + movie + ".");
                } else {
                    System.out.println("About actor " + actorName +" we don't have any information!");
                }
            } while (resultSet.next());
        }
    }


    public static void getAllMoviesCountByDirector(Connection connection, String directorName) throws SQLException {
        PreparedStatement pStmt = connection.prepareStatement("{call getAllMoviesCountByDirector(?)}");
        pStmt.setString(1, directorName);

        ResultSet resultSet = pStmt.executeQuery();
        while (resultSet.next()) {
            String name = resultSet.getString(1);
            int moviesCount = resultSet.getInt(2);
            if (name != null && moviesCount != 0) {
                System.out.println("Director " + name + " has been directed " + moviesCount + " movies.");
            } else {
                System.out.println("About director " + directorName + " we don't have any information!");
            }
        }
    }


    public static void getActorMovieAppCountByMovieDirector(Connection connection, String actorName, String directorName) throws SQLException {
        CallableStatement cStmt = connection.prepareCall("{call getActorMovieAppCountByMovieDirector(?, ?)}");
        cStmt.setString(1, actorName);
        cStmt.setString(2, directorName);

        ResultSet resultSet = cStmt.executeQuery();

        while (resultSet.next()) {
            String name = resultSet.getString(1);
            int movieCount = resultSet.getInt(4);
            String nameOfDirector = resultSet.getString(3);
            if (resultSet.getInt(4) != 0) {
                System.out.println("Actor " + name + " has acted in " + movieCount + " movie directed by " + nameOfDirector + ".");
            } else {
                System.out.println("Actor " + actorName + " has not acted in any movie directed by " + directorName + ".");
            }
        }
    }


    public static void getDirectorProfit(Connection connection,String directorName, String startDate, String endDate) throws SQLException {
        PreparedStatement pStmt = connection.prepareStatement("{call getDirectorProfit(?, ?, ?)}");
        pStmt.setString(1, directorName);
        pStmt.setString(2, startDate);
        pStmt.setString(3, endDate);
        ResultSet resultSet = pStmt.executeQuery();

        while (resultSet.next()) {
            String name = resultSet.getString(1);
            double profit = resultSet.getDouble(2);

            DecimalFormat df = new DecimalFormat("#.####");
            String roundedProfit = df.format(profit);

            if (name != null) {
                System.out.println("Director " + name + " has " +roundedProfit+ " profit from " + startDate +" to "+endDate+".");
            } else {
                System.out.println("About director " + directorName +" we don't have any information!");
            }
        }
    }
}