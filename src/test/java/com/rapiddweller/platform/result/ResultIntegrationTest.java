/* (c) Copyright 2021 by Volker Bergmann. All rights reserved. */

package com.rapiddweller.platform.result;

import com.rapiddweller.benerator.test.AbstractBeneratorIntegrationTest;
import com.rapiddweller.common.FileUtil;
import org.junit.After;
import org.junit.Test;

import java.io.File;

/**
 * Tests the {@link Result} class.<br/><br/>
 * Created: 13.12.2021 16:20:18
 * @author Volker Bergmann
 * @since 2.1.0
 */
public class ResultIntegrationTest extends AbstractBeneratorIntegrationTest {

  @After
  public void tearDown() {
    FileUtil.deleteDirectory(new File("results"));
  }

  @Test
  public void test() {
    parseAndExecuteFile(getClass().getPackageName().replace('.', '/') + "/result.ben.xml");
  }

}