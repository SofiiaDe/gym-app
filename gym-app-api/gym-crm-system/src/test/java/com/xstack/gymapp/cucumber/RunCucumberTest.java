package com.xstack.gymapp.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {"pretty", "html:target/cucumber-report.html"},
    glue = {"com.xstack.gymapp"},
    monochrome = true,
    features = {"src/test/resources"}
)
@CucumberContextConfiguration
@SpringBootTest
public class RunCucumberTest {

}
