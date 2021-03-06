package com.wingnest.play2.jackrabbit;

/*
 * Copyright since 2013 Shigeru GOUGI (sgougi@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.util.HashSet;
import java.util.Set;

import org.jcrom.Jcrom;
import org.jcrom.annotations.JcrNode;

import play.Play;

import com.wingnest.play2.jackrabbit.plugin.ConfigConsts;
import com.wingnest.play2.jackrabbit.plugin.JackrabbitLogger;
import com.wingnest.play2.jackrabbit.plugin.utils.TypeUtils;

public class JCROM {

	public static Jcrom createJcrom() {
		final Set<Class<?>> classes = new HashSet<Class<?>>();
		if ( Play.isDev() ) {
			Nodes.nodes.clear();
			Nodes.refresh();
		}
		classes.addAll(Nodes.nodes);
		return new Jcrom(classes);
	}	
	
	final private static class Nodes {
		private final static Set<Class<?>> nodes;
		static {
			nodes = new HashSet<Class<?>>();
			if ( !Play.isDev() )
				refresh();
		}
		private static void refresh() {
			JackrabbitLogger.debug("Nodes.refresh");
			for ( final  Class<?> javaClass : TypeUtils.getSubTypesOf(Play.application(), ConfigConsts.MODELS_PACKAGE, null) ) {
				if ( javaClass.isAnnotationPresent(JcrNode.class) ) {
					nodes.add(javaClass);
					JackrabbitLogger.debug("registered Node class : %s ", javaClass.getName());
				}
			}
		}
	}
	
}
