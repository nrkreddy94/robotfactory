package de.tech26.robotfactory.repo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import de.tech26.robotfactory.model.Component;
import de.tech26.robotfactory.utils.ComponentCategoryUtil;


/**
 * StockRepo for components stock repository, can perform CRUD operation on stocks
 * 
 * @author Jagadheeswar Reddy
 *
 */
@Repository
public class StockRepo {
	private static final Log logger = LogFactory.getLog(StockRepo.class);
	@Autowired
	private ComponentCategoryUtil componentCategoryUtil;

	@Value("${stocks.file.path}")
	private String stocksPath;

	private static Map<Character, Component> stocks = new HashMap<>();

	@PostConstruct
	private void init() {
		stocks = this.readInputFile(stocksPath);
		categorizeComponents(stocks);
	}

	public List<Component> getAllComponents() {
		return stocks.values().stream().collect(Collectors.toList());
	}

	public Component getComponentByCode(Character code) {
		return stocks.get(code);
	}

	public void updateStocks(Character code, Component component) {
		
		// check if the component is new then categorize it
		if(!stocks.containsKey(code))
			componentCategoryUtil.categorizeComponent(component);
		
		stocks.put(code, component);
		
	}
	public Component deleteComponentByCode(Character code) {
		return stocks.remove(code);
	}

	/**
	 * Takes input file path and read data line by line and convert it to Component
	 * object Map
	 * 
	 * @param filePath input file path
	 * @return Map<Character, Component> return components
	 */
	public Map<Character, Component> readInputFile(String filePath) {
		var components = new HashMap<Character, Component>();
		// read file into stream of lines
		try (Stream<String> lines = Files.lines(Paths.get(ResourceUtils.getFile("classpath:" + filePath).getPath()))) {

			lines.forEach(line -> {
				// Split the line with multiple space using regex \s{2,}+
				String columns[] = line.split("\\s{2,}");
				try {
					Component component = new Component();
					component.setCode(columns[0].charAt(0));
					component.setPrice(Double.valueOf(columns[1]));
					component.setAvailable(Integer.valueOf(columns[2]));
					component.setName(columns[3]);

					components.put(component.getCode(), component);
				} catch (NumberFormatException e) {
					// Catch error about parsing columns
					logger.error("Exception Occured during parse the columns : " + e);
				}

			});

		} catch (IOException e) {
			// Catch error about file path like invalid file path etc.
			logger.error("Exception Occured during read the input file: " + e);
		}

		return components;
	}

	
	/**
	 * Takes stocks as input param and group them to face,arms,mobility and material
	 * components
	 * 
	 * @param stocks
	 */
	private void categorizeComponents(Map<Character, Component> stocks) {
		if (!stocks.isEmpty())
			stocks.values().stream().forEach(component -> {
				componentCategoryUtil.categorizeComponent(component);
			});
	}
}
