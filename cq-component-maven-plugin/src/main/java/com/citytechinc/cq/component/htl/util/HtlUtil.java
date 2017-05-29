/**
 *    Copyright 2017 ICF Olson
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.citytechinc.cq.component.htl.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.texen.util.FileUtil;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.content.Content;
import com.citytechinc.cq.component.content.factory.ContentFactory;
import com.citytechinc.cq.component.dialog.ComponentNameTransformer;
import com.citytechinc.cq.component.dialog.exception.InvalidComponentClassException;
import com.citytechinc.cq.component.dialog.exception.OutputFailureException;
import com.citytechinc.cq.component.maven.util.ComponentMojoUtil;

import javassist.CtClass;
import javassist.CtField;

public class HtlUtil {

	private HtlUtil() {

	};



	/**
	 * Write the content.xml to an output file, the path of which is determined
	 * by the component class
	 * 
	 * @param content
	 * @param componentClass
	 * @return The written file
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws OutputFailureException
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 */
	public static File writeContentToFile(ComponentNameTransformer transformer, File file,
			CtClass componentClass, File buildDirectory, String componentPathBase, String defaultComponentPathSuffix)
					throws TransformerException, ParserConfigurationException, IOException, OutputFailureException,
					ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException,
					InvocationTargetException, NoSuchMethodException {

		return ComponentMojoUtil.writeElementToFile(transformer, file, componentClass, buildDirectory,
				componentPathBase, defaultComponentPathSuffix, "\\"+componentClass.getSimpleName().toLowerCase() + ".html");
	}

	/**
	 * Writes a provided content file to a provided archive output stream at a
	 * path determined by the class of the component.
	 * 
	 * @param contentFile
	 * @param componentClass
	 * @param archiveStream
	 * @param reservedNames A list of files which already exist within the Zip
	 *            Archive. If a .content.xml file already exists for a
	 *            particular component, it is left untouched.
	 * @param componentPathBase
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void writeContentToArchiveFile(ComponentNameTransformer transformer, File contentFile,
			CtClass componentClass, ZipArchiveOutputStream archiveStream, Set<String> reservedNames,
			String componentPathBase, String defaultComponentPathSuffix) throws IOException, ClassNotFoundException {

		ComponentMojoUtil.writeElementToArchiveFile(transformer, contentFile, componentClass, archiveStream,
				reservedNames, componentPathBase, defaultComponentPathSuffix, "\\"+componentClass.getSimpleName().toLowerCase() + ".html");
	}

	/**
	 * Constructs a list of Content objects representing .html files from
	 * a list of Classes. For each Class annotated with a Component annotation a
	 * Content object is constructed.
	 * 
	 * @param classList
	 * @param zipOutputStream
	 * @param reservedNames
	 * @return The constructed Content objects
	 * @throws InvalidComponentClassException
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws OutputFailureException
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 */
	public static List<Content> buildHtlFromClassList(List<CtClass> classList,
			ZipArchiveOutputStream zipOutputStream, Set<String> reservedNames, File buildDirectory,
			String componentPathBase, String defaultComponentPathSuffix, String defaultComponentGroup,
			ComponentNameTransformer transformer) throws InvalidComponentClassException, TransformerException,
	ParserConfigurationException, IOException, OutputFailureException, ClassNotFoundException,
	IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException,
	NoSuchMethodException {

		List<Content> builtContents = new ArrayList<Content>();

		for (CtClass curClass : classList) {
			ComponentMojoUtil.getLog().debug("Checking class for Component annotation " + curClass);

			Component annotation = (Component) curClass.getAnnotation(Component.class);

			ComponentMojoUtil.getLog().debug("Annotation : " + annotation);

			if (annotation != null) {
				ComponentMojoUtil.getLog().debug("Processing Component Class " + curClass);

				File contentFile = createHtlForClass(curClass, buildDirectory);

				writeContentToFile(transformer, contentFile, curClass, buildDirectory, componentPathBase,
				defaultComponentPathSuffix);

				writeContentToArchiveFile(transformer, contentFile, curClass, zipOutputStream, reservedNames,
						componentPathBase, defaultComponentPathSuffix);
			}
		}

		return builtContents;

	}


	private static File createHtlForClass(CtClass componentClass, File buildDirectory) throws ClassNotFoundException, InvalidComponentClassException, IOException {
		// TODO Auto-generated method stub

		Component annotation = (Component) componentClass.getAnnotation(Component.class);

		ComponentMojoUtil.getLog().debug("Annotation : " + annotation);
		ComponentMojoUtil.getLog().debug("Processing Component Class " + componentClass);

		String filePath = buildDirectory.getParentFile().getAbsolutePath()+"\\"+componentClass.getSimpleName()+".vm";

		ComponentMojoUtil.getLog().debug("Using the vm template " + filePath);	
		ComponentMojoUtil.getLog().debug("modelname "+componentClass.getSimpleName());

		ArrayList<String> fieldList = new ArrayList<String>();

		for (CtField field : componentClass.getDeclaredFields()){
			fieldList.add(field.getName());
			ComponentMojoUtil.getLog().debug(field.getName());
		}

		ComponentMojoUtil.getLog().debug("attributes "+fieldList.toString());

		VelocityEngine velocity = new VelocityEngine();	
		velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
		velocity.setProperty("file.resource.loader.class", org.apache.velocity.runtime.resource.loader.FileResourceLoader.class.getName());
		velocity.setProperty("file.resource.loader.path", buildDirectory.getParentFile().getAbsolutePath()+"\\templates");
		velocity.setProperty("file.resource.loader.cache", true);
		velocity.init();
		
		ComponentMojoUtil.getLog().debug("resourcepath "+velocity.getProperty("file.resource.loader.path"));
		
		
		
		Template template;
		if(FileUtil.file(velocity.getProperty("file.resource.loader.path") + componentClass.getSimpleName()+".vm").exists()) {
			template = velocity.getTemplate(componentClass.getSimpleName()+".vm");
		} else {
			template = velocity.getTemplate("default.vm");
		}

		VelocityContext context = new VelocityContext();		
		context.put("modelname", componentClass.getSimpleName());
		context.put("packagename", componentClass.getPackageName());
		context.put("attributes", fieldList);

		Writer writer = new StringWriter();
		template.merge( context, writer );

		File newFile = new File(buildDirectory+"\\temp.txt");
		FileWriter fw = new FileWriter(newFile);
		fw.write(writer.toString());
		fw.close();
		writer.flush();

		return newFile;

	}

}
