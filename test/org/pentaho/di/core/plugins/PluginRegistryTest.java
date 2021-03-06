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

package org.pentaho.di.core.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.pentaho.di.core.exception.KettlePluginException;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.tableinput.TableInputMeta;
import org.pentaho.di.trans.steps.tableoutput.TableOutputMeta;

public class PluginRegistryTest extends TestCase
{
	private static final String	PLUGIN_INPUT_CATEGORY	= "Input";
	private static final String	PLUGIN_OUTPUT_CATEGORY	= "Output";

	private static final String	TABLE_INPUT_PLUGIN_ID	= "TableInput";
	private static final String	TABLE_INPUT_PLUGIN_NAME	= "Table Input";
	private static final String	TABLE_INPUT_PLUGIN_DESCRIPTION	= "The table input step";
	private static final String	TABLE_INPUT_PLUGIN_IMAGE_FILE_NAME	= "/ui/images/TIN.png";

	private static final String	TABLE_OUTPUT_PLUGIN_ID	= "TableOutput";
	private static final String	TABLE_OUTPUT_PLUGIN_NAME	= "Table Output";
	private static final String	TABLE_OUTPUT_PLUGIN_DESCRIPTION	= "The table output step";
	private static final String	TABLE_OUTPUT_PLUGIN_IMAGE_FILE_NAME	= "/ui/images/TOP.png";

	public void testPluginRegistry() throws KettlePluginException {
		PluginRegistry registry = PluginRegistry.getInstance();
		assertNotNull("Registry singleton was not found!", registry);
		
		// PluginRegistry.init() may have already been called, our test path will be different
		// for each case. If it has not been called, try to register the type and plugin. If
		// it has been called, simply verify the plugin type and plugin have been registered.
		Class<? extends PluginTypeInterface> pluginTypeClass = StepPluginType.class;
		Class<?> pluginClass = TableInputMeta.class;
		
		try {
		  registry.getPluginType(pluginTypeClass);
		}
		catch(KettlePluginException kpe) {
		  // Register a new plugin type...
	    registry.registerPluginType(pluginTypeClass);
		}
		
		// See if the plugin is there...
		try {
      registry.getPluginType(pluginTypeClass);
    }
    catch(KettlePluginException kpe) {
      fail(pluginTypeClass.getName()+" expected in the PluginRegistry but was not found!");
    }
		
		PluginInterface plugin = registry.getPlugin(pluginTypeClass, pluginClass);
		Map<Class<?>, String> classMap = new HashMap<Class<?>, String>();
		PluginInterface tableInputPlugin = plugin;
		if(plugin == null) {
  		// Register a single step plugin
  		//
  		classMap.put(pluginClass, "org.pentaho.di.trans.steps.tableinput.TableInputMeta");
  		tableInputPlugin = new Plugin(
  					new String[] { TABLE_INPUT_PLUGIN_ID, }, 
  					pluginTypeClass, 
  					StepMetaInterface.class,
  					PLUGIN_INPUT_CATEGORY, 
  					TABLE_INPUT_PLUGIN_NAME, 
  					TABLE_INPUT_PLUGIN_DESCRIPTION, 
  					TABLE_INPUT_PLUGIN_IMAGE_FILE_NAME,
  					false, 
  					true, 
  					classMap,
  					new ArrayList<String>(),
  					null, // No error help file
  					null, // pluginFolder
  					null, // documentation URL
            null, // cases URL
            null  // forum URL
  				);
  		registry.registerPlugin(pluginTypeClass, tableInputPlugin);
  		plugin = registry.getPlugin(pluginTypeClass, pluginClass);
		}

	  // Verify the plugin has been registered
    PluginInterface verify = registry.getPlugin(pluginTypeClass, TABLE_INPUT_PLUGIN_ID);
		assertNotNull("A plugin was not found in the plugin registry", verify);
		assertEquals("A different plugin then expected was retrieved from the plugin registry", verify, tableInputPlugin);
		
		pluginClass = TableOutputMeta.class;
		plugin = registry.getPlugin(pluginTypeClass, pluginClass);
    PluginInterface tableOutputPlugin = plugin;
    if(plugin == null) {
  		// Register a second step plugin
  		//
  		classMap = new HashMap<Class<?>, String>();
  		classMap.put(TableOutputMeta.class, "org.pentaho.di.trans.steps.tableoutput.TableOutputMeta");
  		tableOutputPlugin = new Plugin(
  					new String[] { TABLE_OUTPUT_PLUGIN_ID, }, 
  					pluginTypeClass, 
  					StepMetaInterface.class,
  					PLUGIN_OUTPUT_CATEGORY, 
  					TABLE_OUTPUT_PLUGIN_NAME, 
  					TABLE_OUTPUT_PLUGIN_DESCRIPTION, 
  					TABLE_OUTPUT_PLUGIN_IMAGE_FILE_NAME,
  					false, 
  					true, 
  					classMap,
  					new ArrayList<String>(),
  					null, // No error help file
  					null, // pluginFolder
  					null, // documentation URL
            null, // cases URL
            null  // forum URL
  				);
  		registry.registerPlugin(pluginTypeClass, tableOutputPlugin);
  		plugin = registry.getPlugin(pluginTypeClass, pluginClass);
    }

    // Verify the plugin has been registered
		verify = registry.getPlugin(pluginTypeClass, TABLE_OUTPUT_PLUGIN_ID);
		assertNotNull("A plugin was not found in the plugin registry", verify);
		assertEquals("A different plugin then expected was retrieved from the plugin registry", verify, tableOutputPlugin);
		
		// Get a list by category...
		//
		List<PluginInterface> inputPlugins = registry.getPluginsByCategory(pluginTypeClass, PLUGIN_INPUT_CATEGORY);
		assertEquals("Exactly one plugin expected in the step plugin input category", 1, inputPlugins.size());
		assertEquals("The table input step was expected in the input category", inputPlugins.get(0), tableInputPlugin);
		assertTrue("Input plugins list should contain the table input step", inputPlugins.contains(tableInputPlugin));
		assertFalse("Input plugins list should not contain the table output step", inputPlugins.contains(tableOutputPlugin));
		
		List<PluginInterface> outputPlugins = registry.getPluginsByCategory(pluginTypeClass, PLUGIN_OUTPUT_CATEGORY);
		assertEquals("Exactly one plugin expected in the step plugin output category", 1, outputPlugins.size());
		assertEquals("The table output step was expected in the otuput category", outputPlugins.get(0), tableOutputPlugin);
		assertTrue("Output plugins list should contain the table output step", outputPlugins.contains(tableOutputPlugin));
		assertFalse("Output plugins list should not contain the table input step", outputPlugins.contains(tableInputPlugin));
		
		// List the categories...
		//
		List<String> categories = registry.getCategories(pluginTypeClass);
		assertEquals("Two categories expected in the step plugin registry", 2, categories.size());
		assertTrue("The input category was expected in the categories list", categories.contains(PLUGIN_INPUT_CATEGORY));
		assertTrue("The output category was expected in the categories list", categories.contains(PLUGIN_OUTPUT_CATEGORY));
		
		// Now have a little bit of class loading fun: load the main class of the plugin
		//
		Object object = registry.loadClass(tableInputPlugin, TableInputMeta.class);
		assertNotNull(object);
		assertTrue(object instanceof TableInputMeta);

		// The same but now explicitly asking for the main class
		//
		Object object2 = registry.loadClass(tableOutputPlugin, TableOutputMeta.class);
		assertNotNull(object2);
		assertTrue(object2 instanceof TableOutputMeta);

		try {
			registry.loadClass(tableInputPlugin, String.class);
			fail("A String class type can't be used when loading a step class");
		} catch(Exception e) {
			// OK!
		}
	}
	
	public void testPluginRegistryInit() throws KettlePluginException {
		
		// Run an init() just to see it doesn't blow up
		//
	  PluginTypeInterface[] plugins = new PluginTypeInterface[] {
      StepPluginType.getInstance(),       // Steps
      PartitionerPluginType.getInstance(),    // Partitioners
      JobEntryPluginType.getInstance(),       // Job entries
      RepositoryPluginType.getInstance(),   // Repository types
      DatabasePluginType.getInstance(),       // Databases
    };
	  for(PluginTypeInterface pl : plugins){
	    PluginRegistry.addPluginType(pl);
	  }
		PluginRegistry.init();
	}
}
