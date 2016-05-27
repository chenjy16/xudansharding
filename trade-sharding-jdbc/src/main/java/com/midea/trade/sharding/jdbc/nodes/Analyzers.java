package com.midea.trade.sharding.jdbc.nodes;

import java.io.IOException;

import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foundationdb.sql.parser.NodeTypes;
import com.foundationdb.sql.parser.QueryTreeNode;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.midea.trade.sharding.core.shard.AnalyzeResult;

/**
 * SQL节点解析器
 */
public final class Analyzers {
	
	static Logger logger = LoggerFactory.getLogger(Analyzers.class);
	
	@SuppressWarnings("unchecked")
	static final NodeAnalyzer<QueryTreeNode, AnalyzeResult> analizerArr[] = new NodeAnalyzer[NodeTypes.MAX_NODE_TYPE];
	static {
		initAnalizers();
	}

	@SuppressWarnings("unchecked")
	private static void initAnalizers() {

		ImmutableSet<ClassInfo> classInfos = null;
		try {
			classInfos = ClassPath.from(Analyzers.class.getClassLoader())
					.getTopLevelClassesRecursive(
							Analyzers.class.getPackage().getName());
			
			for (ClassInfo classInfo : classInfos) {
				Class<?> claz = classInfo.load();
				try {

					if (NodeAnalyzer.class.isAssignableFrom(claz)
							&& !claz.isInterface()
							&& !Modifier.isAbstract(claz.getModifiers())) {
						register((NodeAnalyzer<QueryTreeNode, AnalyzeResult>) claz
								.newInstance());
					}
				} catch (Exception e) {
					logger.error("Analyzers  SQL节点解析器", e.getMessage());
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			logger.error("Analyzers  SQL节点解析器", e1.getMessage());
			e1.printStackTrace();
		}

	}

	private static void register(
			NodeAnalyzer<QueryTreeNode, AnalyzeResult> analizer) {
		if (analizer.getNodeTypes() == null
				|| analizer.getNodeTypes().length <= 0) {
			return;
		}
		for (int nodeType : analizer.getNodeTypes()) {
			analizerArr[nodeType] = analizer;
		}

	}

	public static NodeAnalyzer<QueryTreeNode, AnalyzeResult> get(int type) {
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer= analizerArr[type];
		if(analyzer==null){
			logger.info("no analyzer found!nodeType="+type);
		}
		return analyzer;
	}
	
	
	public static NodeAnalyzer<QueryTreeNode, AnalyzeResult> get(QueryTreeNode node) {
		return get(node.getNodeType());
	}
	
	

	public static void main(String args[]) {
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analizer = Analyzers.get(NodeTypes.SELECT_NODE);
		
		System.out.println(analizer.getClass() + " nodeType="+ analizer.getNodeTypes() + " input=" + NodeTypes.SELECT_NODE);

	}
}
