/*
 * (c) Copyright 2006-2021 by rapiddweller GmbH & Volker Bergmann. All rights reserved.
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

package com.rapiddweller.benerator.engine.parser.xml;

import com.rapiddweller.benerator.BeneratorErrorIds;
import com.rapiddweller.benerator.engine.BeneratorContext;
import com.rapiddweller.benerator.engine.BeneratorRootStatement;
import com.rapiddweller.benerator.engine.Statement;
import com.rapiddweller.benerator.engine.statement.IfStatement;
import com.rapiddweller.benerator.factory.BeneratorExceptionFactory;
import com.rapiddweller.benerator.parser.ModelParser;
import com.rapiddweller.common.CollectionUtil;
import com.rapiddweller.common.xml.XMLUtil;
import com.rapiddweller.format.xml.AttrInfoSupport;
import com.rapiddweller.model.data.ComponentDescriptor;
import org.w3c.dom.Element;

import java.util.Collection;

import static com.rapiddweller.benerator.engine.DescriptorConstants.EL_DEFAULT_COMPONENTS;

/**
 * Parses a &lt;defaultComponents&gt; element in a Benerator descriptor file.<br/><br/>
 * Created: 25.10.2009 00:17:04
 * @author Volker Bergmann
 * @since 0.6.0
 */
public class DefaultComponentParser extends AbstractBeneratorDescriptorParser {

  static final Collection<String> COMPONENT_TYPES = CollectionUtil.toSet("attribute", "part", "id", "reference");

  private static final AttrInfoSupport ATTR_SUPPORT;
  static {
    ATTR_SUPPORT = new AttrInfoSupport(BeneratorErrorIds.SYN_DEFAULT_COMPONENTS);
  }

  public DefaultComponentParser() {
    super(EL_DEFAULT_COMPONENTS, ATTR_SUPPORT, BeneratorRootStatement.class, IfStatement.class);
  }

  @Override
  public XMLDefaultComponentsStatement doParse(
      Element element, Element[] parentXmlPath, Statement[] parentComponentPath, BeneratorParseContext context) {
    return new XMLDefaultComponentsStatement(element);
  }

  static class XMLDefaultComponentsStatement implements Statement {

    private final Element element;

    public XMLDefaultComponentsStatement(Element element) {
      this.element = element;
    }

    @Override
    public boolean execute(BeneratorContext context) {
      for (Element child : XMLUtil.getChildElements(element)) {
        String childType = XMLUtil.localName(child);
        if (COMPONENT_TYPES.contains(childType)) {
          ModelParser parser = new ModelParser(context, true);
          ComponentDescriptor component = parser.parseSimpleTypeComponent(child, null, null);
          context.setDefaultComponentConfig(component);
        } else {
          throw BeneratorExceptionFactory.getInstance().configurationError("Unexpected element: " + childType);
        }
      }
      return true;
    }

  }

}
