package com.midea.trade.sharding.config.parser;
import org.w3c.dom.Element;
import com.midea.trade.sharding.config.TrackerConfig;
import com.midea.trade.sharding.core.exception.ConfigurationException;
import com.midea.trade.sharding.core.timetracker.TrackPoint;
import com.midea.trade.sharding.core.timetracker.Tracker;
import com.midea.trade.sharding.core.timetracker.TrackerHodler;



/**
 * Tracker配置解析器
 */
public class TrackerConfigParser implements Parser<TrackerConfig> {

	@Override
	public TrackerConfig parse(Element el) {
		TrackerConfig trackerConfig = new TrackerConfig();
		TrackerHodler trackerHodler = new TrackerHodler();
		
		String type = ParseUtils.getAttr(el, "type");
		trackerConfig.setType(type);
		trackerHodler.setTrackPoint(TrackPoint.parse(type));
		
		String threshold = ParseUtils.getAttr(el, "threshold");
		trackerConfig.setThreshold(Long.valueOf(threshold));
		trackerHodler.setThreshold(Long.valueOf(threshold));
		
		try {
			String className = ParseUtils.getAttr(el, "class");
			trackerConfig.setClassName(className);
			Tracker tracker = (Tracker) Class.forName(className).newInstance();
			trackerHodler.setTracker(tracker);
		} catch (Exception e){
			throw new ConfigurationException(e);
		}
		
		trackerHodler.regist();
		return trackerConfig;
	}

}
