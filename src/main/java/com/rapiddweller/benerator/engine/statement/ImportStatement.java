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

package com.rapiddweller.benerator.engine.statement;

import com.rapiddweller.benerator.BeneratorFactory;
import com.rapiddweller.benerator.DomainDescriptor;
import com.rapiddweller.benerator.PlatformDescriptor;
import com.rapiddweller.benerator.engine.BeneratorContext;
import com.rapiddweller.benerator.engine.Importer;
import com.rapiddweller.benerator.engine.Statement;

/**
 * Imports classes by package, class, domain and platform definition(s).<br/><br/>
 * Created at 22.07.2009 08:19:54
 * @author Volker Bergmann
 * @since 0.6.0
 */
public class ImportStatement implements Statement {

  private final boolean defaultImports;
  private final String classImport;
  private final DomainDescriptor[] domainImports;
  private final PlatformDescriptor[] platformImports;

  public ImportStatement(boolean defaultImports, String classImport,
                         DomainDescriptor[] domainImports, PlatformDescriptor[] platformImports) {
    this.defaultImports = defaultImports;
    this.classImport = classImport;
    this.domainImports = domainImports;
    this.platformImports = platformImports;
  }

  @Override
  public boolean execute(BeneratorContext context) {
    if (defaultImports) {
      BeneratorFactory.getInstance().importDefaultClasses(context);
    }

    if (classImport != null) {
      context.importClass(classImport);
    }

    if (domainImports != null) {
      for (DomainDescriptor domainImport : domainImports) {
        importDomain(domainImport, context);
      }
    }

    if (platformImports != null) {
      for (PlatformDescriptor platformImport : platformImports) {
        Importer.importPlatformClasses(platformImport, context);
      }
    }
    return true;
  }

  public void importDomain(DomainDescriptor domain, BeneratorContext context) {
    context.importPackage(domain.getPackage());
  }

}
