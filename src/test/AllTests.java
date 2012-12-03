package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CornerAnalysisTests.class, CornerTests.class, CornerSorterTest.class, BotAssignmentTest.class })
public class AllTests {}
