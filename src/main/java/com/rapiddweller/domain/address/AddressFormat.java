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

package com.rapiddweller.domain.address;

import com.rapiddweller.common.Context;
import com.rapiddweller.common.IOUtil;
import com.rapiddweller.common.context.DefaultContext;
import com.rapiddweller.common.converter.UnsafeConverter;
import com.rapiddweller.common.exception.ExceptionFactory;
import com.rapiddweller.format.script.Script;
import com.rapiddweller.format.script.freemarker.FreeMarkerScriptFactory;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Formats Address objects as String.<br/><br/>
 * Created: 06.04.2008 14:15:25
 * @author Volker Bergmann
 */
public class AddressFormat {

  private static final FreeMarkerScriptFactory SCRIPT_FACTORY = new FreeMarkerScriptFactory();

  static final Map<String, AddressFormat> instances = new HashMap<>();
  private static final String CONFIG_FILE = "/com/rapiddweller/domain/address/addressFormat.properties";

  public static final AddressFormat US = getInstance("US");
  public static final AddressFormat AU = getInstance("AU");
  public static final AddressFormat DE = getInstance("DE");
  public static final AddressFormat BE = getInstance("BE");

  private final String pattern;
  private final Script script;

  public AddressFormat(String pattern) {
    this.pattern = pattern;
    script = SCRIPT_FACTORY.parseText(pattern);
  }

  @SuppressWarnings("rawtypes")
  public static AddressFormat getInstance(String country) {
    if (instances.isEmpty()) {
      try {
        IOUtil.readProperties(CONFIG_FILE,
            new UnsafeConverter<>(Map.Entry.class, Map.Entry.class) {
              @Override
              public Entry convert(Entry entry) {
                String pt = (String) entry.getValue();
                instances.put((String) entry.getKey(), new AddressFormat(pt));
                return entry;
              }
            });
      } catch (Exception e) {
        throw ExceptionFactory.getInstance().configurationError(
            "Error while processing AddressFormat configuration", e);
      }
    }
    return instances.get(country);
  }

  public String getPattern() {
    return pattern;
  }

  public String format(Address address) {
    try {
      Context context = new DefaultContext();
      context.set("address", address);
      StringWriter out = new StringWriter();
      script.execute(context, out);
      return out.toString().trim();
    } catch (Exception e) {
      throw ExceptionFactory.getInstance().internalError(
          "Error evaluating script '" + script + "'", e);
    }
  }

}
