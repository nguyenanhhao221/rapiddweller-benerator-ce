/*
 * (c) Copyright 2006-2020 by rapiddweller GmbH & Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License.
 *
 * For redistributing this software or a derivative work under a license other
 * than the GPL-compatible Free Software License as defined by the Free
 * Software Foundation or approved by OSI, you must first obtain a commercial
 * license to this software product from rapiddweller GmbH & Volker Bergmann.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED CONDITIONS,
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE
 * HEREBY EXCLUDED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.rapiddweller.domain.organization;

import com.rapiddweller.benerator.test.GeneratorClassTest;
import com.rapiddweller.common.ConfigurationError;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 * Tests the CompanyNameGenerator.<br/><br/>
 * Created: 14.03.2008 08:31:26
 *
 * @author Volker Bergmann
 */
public class CompanyNameGeneratorTest extends GeneratorClassTest {

  private static final Logger logger = LoggerFactory.getLogger(CompanyNameGeneratorTest.class);

  @Test
  public void testConstructor() {
    CompanyNameGenerator actualCompanyNameGenerator = new CompanyNameGenerator("Dataset");
    assertEquals("Dataset", actualCompanyNameGenerator.getDataset());
    assertTrue(actualCompanyNameGenerator.isSector());
    assertTrue(actualCompanyNameGenerator.isLocation());
    assertTrue(actualCompanyNameGenerator.isLegalForm());
    assertEquals(0.0, actualCompanyNameGenerator.getWeight(), 0.0);
    assertNull(actualCompanyNameGenerator.getSource());
    assertEquals("/com/rapiddweller/dataset/region", actualCompanyNameGenerator.getNesting());
    Class<?> expectedGeneratedType = CompanyName.class;
    assertSame(expectedGeneratedType, actualCompanyNameGenerator.getGeneratedType());
  }

  @Test
  public void testConstructor2() {
    CompanyNameGenerator actualCompanyNameGenerator = new CompanyNameGenerator(true, true, true, "Dataset Name");
    actualCompanyNameGenerator.setLegalForm(false);
    actualCompanyNameGenerator.setLocation(false);
    actualCompanyNameGenerator.setSector(false);
    assertEquals("class com.rapiddweller.domain.organization.CompanyName", actualCompanyNameGenerator.getGeneratedType().toString());
    assertEquals("CompanyNameGenerator[/com/rapiddweller/dataset/region:Dataset Name]", actualCompanyNameGenerator.toString());
    assertEquals("Dataset Name", actualCompanyNameGenerator.getDataset());
    assertFalse(actualCompanyNameGenerator.isSector());
    assertFalse(actualCompanyNameGenerator.isLocation());
    assertFalse(actualCompanyNameGenerator.isLegalForm());
    assertTrue(actualCompanyNameGenerator.isThreadSafe());
    assertTrue(actualCompanyNameGenerator.isParallelizable());
    assertEquals(0.0, actualCompanyNameGenerator.getWeight(), 0.0);
    assertNull(actualCompanyNameGenerator.getSource());
    assertEquals("/com/rapiddweller/dataset/region", actualCompanyNameGenerator.getNesting());
    Class<?> expectedGeneratedType = CompanyName.class;
    assertSame(expectedGeneratedType, actualCompanyNameGenerator.getGeneratedType());
  }

  public CompanyNameGeneratorTest() {
    super(CompanyNameGenerator.class);
  }

  @Test
  public void testGermany() {
    check("DE");
  }

  @Test
  public void testUSA() {
    check("US");
  }

  @Test
  public void testBrazil() {
    check("BR");
  }

  @Test(expected = ConfigurationError.class)
  public void testXX() {
    check("XX");
  }

  @Test
  public void testGenerateForDACH() {
    CompanyNameGenerator generator = new CompanyNameGenerator("dach");
    generator.init(context);
    for (int i = 0; i < 100; i++) {
      CompanyName name = generator.generate();
      if (logger.isDebugEnabled()) {
        logger.debug(name.toString());
      }
      assertNotNull(name);
      assertTrue(name.toString().length() > 1);
      System.out.println(name);
    }
  }

  @Test
  public void testWorld() {
    CompanyNameGenerator generator = new CompanyNameGenerator("world");
    generator.init(context);
    for (int i = 0; i < 100; i++) {
      CompanyName name = generator.generate();
      if (logger.isDebugEnabled()) {
        logger.debug(name.toString());
      }
      assertNotNull(name);
      assertTrue(name.toString().length() > 1);
      System.out.println(name);
    }
  }

  public void check(String dataset) {
    CompanyNameGenerator generator = new CompanyNameGenerator(dataset);
    generator.init(context);
    for (int i = 0; i < 100; i++) {
      CompanyName name = generator.generate();
      if (logger.isDebugEnabled()) {
        logger.debug(name.toString());
      }
      assertNotNull(name);
      assertTrue(name.toString().length() > 1);
    }
  }

}
