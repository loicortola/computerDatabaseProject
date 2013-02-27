package com.formation.project.computerDatabase.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.formation.project.computerDatabase.exception.DAOConfigurationException;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class DaoFactory {
    private static final String FICHIER_PROPERTIES       = "com/formation/project/computerDatabase/dao/dao.properties";

    private static final String PROPERTY_URL             = "url";
    private static final String PROPERTY_DRIVER          = "driver";
    private static final String PROPERTY_NOM_UTILISATEUR = "user";
    private static final String PROPERTY_MOT_DE_PASSE    = "password";
	
	protected BoneCP connectionPool 				 	 = null;
	
	public DaoFactory(BoneCP connectionPool) {
		this.connectionPool = connectionPool;
	}
	
	public ICompanyDao getCompanyDao() {
		return new JdbcDbCompanyDao(this);
	}

	public IComputerDao getComputerDao() {
		return new JdbcDbComputerDao(this);
	}
	
	public IStatsDao getStatsDao() {
		return new JdbcDbStatsDao(this);
	}
	
	public static DaoFactory getInstance() {
		Properties properties = new Properties();
        String url;
        String driver;
        String nomUtilisateur;
        String motDePasse;
        BoneCP connectionPool = null;
 
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );
 
        if ( fichierProperties == null ) {
            throw new DAOConfigurationException( "Cannot find properties file " + FICHIER_PROPERTIES );
        }
 
        try {
            properties.load( fichierProperties );
            url = properties.getProperty( PROPERTY_URL );
            driver = properties.getProperty( PROPERTY_DRIVER );
            nomUtilisateur = properties.getProperty( PROPERTY_NOM_UTILISATEUR );
            motDePasse = properties.getProperty( PROPERTY_MOT_DE_PASSE );
        } catch ( FileNotFoundException e ) {
        	throw new DAOConfigurationException( "Cannot find properties file " + FICHIER_PROPERTIES , e);
        } catch ( IOException e ) {
            throw new DAOConfigurationException( "Cannot load properties file " + FICHIER_PROPERTIES, e );
        }
 
        try {
            Class.forName( driver );
        } catch ( ClassNotFoundException e ) {
            throw new DAOConfigurationException( "Cannot find driver in classpath", e );
        }
 
        try {
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl( url );
            config.setUsername( nomUtilisateur );
            config.setPassword( motDePasse );
            config.setMinConnectionsPerPartition( 5 );
            config.setMaxConnectionsPerPartition( 10 );
            config.setPartitionCount( 2 );
            
            connectionPool = new BoneCP( config );
            
        } catch ( SQLException e ) {
            e.printStackTrace();
            throw new DAOConfigurationException( "Configuration error in Connection Pool", e );
        }
        DaoFactory instance = new DaoFactory( connectionPool );
        return instance;
	}
	
	public Connection getConn() {
        try {
			return connectionPool.getConnection();
		} catch (SQLException e) {
			System.err.println("Error in DaoFactory.getConn: " + e.getMessage());
		}
        return null;
    }
	
	public static void closeConn(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			System.err.println("Error in DaoFactory.closeConn: " + e.getMessage());
		}
	}
}
