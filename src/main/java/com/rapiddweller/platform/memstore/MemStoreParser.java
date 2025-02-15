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

package com.rapiddweller.platform.memstore;

import com.rapiddweller.benerator.BeneratorErrorIds;
import com.rapiddweller.benerator.engine.BeneratorRootStatement;
import com.rapiddweller.benerator.engine.Statement;
import com.rapiddweller.benerator.engine.parser.xml.AbstractBeneratorDescriptorParser;
import com.rapiddweller.benerator.engine.parser.xml.BeneratorParseContext;
import com.rapiddweller.benerator.engine.parser.xml.DescriptorParserUtil;
import com.rapiddweller.benerator.engine.statement.IfStatement;
import com.rapiddweller.benerator.factory.BeneratorExceptionFactory;
import com.rapiddweller.common.ConversionException;
import com.rapiddweller.common.StringUtil;
import com.rapiddweller.common.xml.XMLUtil;
import com.rapiddweller.format.xml.AttrInfoSupport;
import org.w3c.dom.Element;

import java.util.Map;

import static com.rapiddweller.benerator.engine.DescriptorConstants.ATT_ID;
import static com.rapiddweller.benerator.engine.DescriptorConstants.EL_MEMSTORE;
import static com.rapiddweller.benerator.engine.parser.xml.DescriptorParserUtil.getAttributeAsString;

/**
 * Parses a &lt;memstore%gt; statement.<br/><br/>
 * Created: 08.03.2011 13:28:55
 * @author Volker Bergmann
 * @since 0.6.6
 */
public class MemStoreParser extends AbstractBeneratorDescriptorParser {

  private static final AttrInfoSupport ATTR_INFO;
  static {
    ATTR_INFO = new AttrInfoSupport(BeneratorErrorIds.SYN_MEMSTORE_ILLEGAL_ATTR);
    ATTR_INFO.add(ATT_ID, true, BeneratorErrorIds.SYN_MEMSTORE_ID);
  }

  public MemStoreParser() {
    super(EL_MEMSTORE, ATTR_INFO, BeneratorRootStatement.class, IfStatement.class);
  }

  @Override
  public MemStoreStatement doParse(Element element, Element[] parentXmlPath, Statement[] parentComponentPath, BeneratorParseContext context) {
    checkAttributeSupport(XMLUtil.getAttributes(element));
    try {
      String id = DescriptorParserUtil.getAttributeAsString(ATT_ID, element);
      return new MemStoreStatement(id, context.getResourceManager());
    } catch (ConversionException e) {
      throw BeneratorExceptionFactory.getInstance().configurationError("Error parsing memstore definition", e);
    }
  }

  private static void checkAttributeSupport(Map<String, String> attributes) {
    if (StringUtil.isEmpty(attributes.get(ATT_ID))) {
      throw BeneratorExceptionFactory.getInstance().configurationError("No id specified for <store>");
    }
    for (String key : attributes.keySet()) {
      if (!"id".equals(key)) {
        throw BeneratorExceptionFactory.getInstance().configurationError("Not a supported attribute of <store>: " + key);
      }
    }
  }

}
