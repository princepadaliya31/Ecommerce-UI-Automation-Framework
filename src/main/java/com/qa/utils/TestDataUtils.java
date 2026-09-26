package com.qa.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * TestDataUtils reads CSV data files and converts them into Object[][] for TestNG DataProviders.
 */
public class TestDataUtils {

    private static final Logger logger = LoggerFactory.getLogger(TestDataUtils.class);

    public static Object[][] getCSVData(String resourcePath) {
        List<Object[]> dataList = new ArrayList<>();

        try (InputStream is = TestDataUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                logger.error("Resource not found: {}", resourcePath);
                return new Object[0][0];
            }

            try (CSVReader reader = new CSVReader(new InputStreamReader(is))) {
                List<String[]> allRows = reader.readAll();
                if (allRows.isEmpty()) {
                    logger.warn("CSV file is empty: {}", resourcePath);
                    return new Object[0][0];
                }

                // Skip header row (index 0)
                for (int i = 1; i < allRows.size(); i++) {
                    dataList.add(allRows.get(i));
                }
            }
        } catch (CsvException e) {
            logger.error("CSV parsing error reading {}", resourcePath, e);
        } catch (Exception e) {
            logger.error("IOException reading {}", resourcePath, e);
        }

        return dataList.toArray(new Object[0][0]);
    }
}
