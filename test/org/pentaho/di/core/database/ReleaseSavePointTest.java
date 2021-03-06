/*******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2012 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.di.core.database;
import static org.junit.Assert.*;

import org.junit.Test;

public class ReleaseSavePointTest {
          
   @Test 
   public void testReleaseSavePointBooleans() {
      try {
         DatabaseInterface databaseInterface;
         
         // Test Oracle - it should not support releasing savepoints
         // Neither does MySQL for that matter.
         //
         databaseInterface = new OracleDatabaseMeta();
         assertFalse(databaseInterface.releaseSavepoint());

         databaseInterface = new MySQLDatabaseMeta();
         assertFalse(databaseInterface.releaseSavepoint());

         databaseInterface = new InfiniDbDatabaseMeta();
         assertFalse(databaseInterface.releaseSavepoint());
         
         databaseInterface = new InfobrightDatabaseMeta();
         assertFalse(databaseInterface.releaseSavepoint());
         

         //  the rest of these should assert to true
         
         databaseInterface = new GreenplumDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new AS400DatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new DB2DatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new DbaseDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new DerbyDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new ExtenDBDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new FirebirdDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new GenericDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new GuptaDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new H2DatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new HypersonicDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new InformixDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new IngresDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());

         databaseInterface = new InterbaseDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());     
         
         databaseInterface = new KingbaseESDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new LucidDBDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new MonetDBDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new MSAccessDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());

         databaseInterface = new MSSQLServerDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new MSSQLServerNativeDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
                  
         databaseInterface = new NeoviewDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());

         databaseInterface = new NetezzaDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new OracleRDBDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new PostgreSQLDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new RemedyActionRequestSystemDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new SAPDBDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new SAPR3DatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new SQLiteDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new SybaseDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new SybaseIQDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new TeradataDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());

         databaseInterface = new UniVerseDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
         
         databaseInterface = new VerticaDatabaseMeta();
         assertTrue(databaseInterface.releaseSavepoint());
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }
}
