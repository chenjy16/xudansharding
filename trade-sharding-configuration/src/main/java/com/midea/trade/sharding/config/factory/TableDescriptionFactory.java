package com.midea.trade.sharding.config.factory;

import com.midea.trade.sharding.config.Configurations;
import com.midea.trade.sharding.config.FunctionConfig;
import com.midea.trade.sharding.config.NameNodeReferenceConfig;
import com.midea.trade.sharding.config.TableConfig;
import com.midea.trade.sharding.core.exception.ConfigurationException;
import com.midea.trade.sharding.core.factory.ObjectFactory;
import com.midea.trade.sharding.core.resources.NameNode;
import com.midea.trade.sharding.core.resources.NameNodeHolder;
import com.midea.trade.sharding.core.resources.TableDescription;
import com.midea.trade.sharding.core.shard.Function;
import com.midea.trade.sharding.core.shard.Script;
import com.midea.trade.sharding.core.shard.ScriptFunction;
import com.midea.trade.sharding.core.shard.ShardType;


/**
 * Table配置工厂
 */
public class TableDescriptionFactory implements ObjectFactory<TableConfig> {

	@Override
	public Object create(TableConfig config) {
		TableDescription desc = new TableDescription(config.getName());
		
		for (NameNodeReferenceConfig ref : config.getReferenceList()) {
			NameNode nameNode = Configurations.getInstance().getNameNodeById(
					ref.getRef());
			if (nameNode == null) {
				throw new ConfigurationException(
						"namenode not found!could not find namenode id=["
								+ ref.getRef() + "],please check your table=["
								+ config.getName() + "] configuration!");
			}
			NameNodeHolder holder = new NameNodeHolder(nameNode);
			holder.setTableName(ref.getTableName());
			holder.setOrgTableName(ref.getOrgTableName());
			holder.setIndex(ref.getIndex());
			holder.setSchema(ref.getSchema());
			desc.addNameNode(holder);
		}
		
		desc.setShardType(config.getShardType());
		desc.setDifferentName(config.isDifferName());
		if(config.getShardType() == ShardType.NO_SHARD) {
			desc.setFunction(Function.NO_SHARD_FUNCTION);
			desc.setColumns(new String[]{});
		} else {
			Function function = createFunction(config.getFunction());
			desc.setFunction(function);
			desc.setColumns(config.getColumns());
		}
		return desc;
	}

	Function createFunction(FunctionConfig config) {
		Function func = null;
		if (config.getScript() != null) {
			func = createScriptFunction(config.getScript());
		} else {
			func = (Function) Configurations.getInstance().getBean(
					config.getRef());
		}
		return func;

	}

	private Function createScriptFunction(Script script) {
		return new ScriptFunction(script);
	}
}
