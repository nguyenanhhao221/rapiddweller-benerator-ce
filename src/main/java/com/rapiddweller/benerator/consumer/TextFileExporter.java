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

package com.rapiddweller.benerator.consumer;

import com.rapiddweller.common.IOUtil;
import com.rapiddweller.common.SystemInfo;
import com.rapiddweller.common.exception.ExceptionFactory;

import java.io.File;
import java.io.PrintWriter;

/**
 * Parent class for Exporters that export data to a text file.<br/><br/>
 * Created: 11.07.2008 09:50:46
 * @author Volker Bergmann
 * @since 0.5.4
 */
public class TextFileExporter extends FormattingConsumer implements FileExporter {

  // attributes ------------------------------------------------------------------------------------------------------

  protected String uri;
  protected String encoding;
  protected String lineSeparator;
  protected boolean append;
  protected boolean wasAppended;

  protected PrintWriter printer;

  // constructors ----------------------------------------------------------------------------------------------------

  public TextFileExporter() {
    this(null, null, null);
  }

  public TextFileExporter(String uri) {
    this(uri, null, null);
  }

  public TextFileExporter(String uri, String encoding, String lineSeparator) {
    this.uri = (uri != null ? uri : "export.txt");
    this.encoding = (encoding != null ? encoding : SystemInfo.getFileEncoding());
    this.lineSeparator = (lineSeparator != null ? lineSeparator : SystemInfo.getLineSeparator());
    this.append = false;
  }

  // callback interface for child classes ----------------------------------------------------------------------------

  /** This method is called after printer initialization and before writing the first data entry.
   *  Overwrite this method in child classes e.g. for writing a file header.
   *  @param data the first data item to write to the file */
  protected void postInitPrinter(Object data) {
    // overwrite this in child classes, e.g. for writing a file header
  }

  /** Writes the data to the output file.
   *  It uses the parent class settings for rendering the object.
   *  Overwrite this in a child class for custom output format.
   *  @param data the data object to output */
  protected void startConsumingImpl(Object data) {
    printer.print(plainConverter.convert(data));
    println();
  }

  /** This method is called after writing the last data entry and before closing the underlying printer.
   *  Overwrite this method in child classes e.g. for writing a file footer.v*/
  protected void preClosePrinter() {
    // overwrite this in child classes, e.g. for writing a file footer
  }

  // properties ------------------------------------------------------------------------------------------------------

  @Override
  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public String getEncoding() {
    return encoding;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  public String getLineSeparator() {
    return lineSeparator;
  }

  public void setLineSeparator(String lineSeparator) {
    this.lineSeparator = lineSeparator;
  }

  public boolean isAppend() {
    return append;
  }

  public void setAppend(boolean append) {
    this.append = append;
  }

  // Consumer interface ----------------------------------------------------------------------------------------------

  @Override
  public final synchronized void startProductConsumption(Object data) {
    if (printer == null) {
      initPrinter(data);
    }
    startConsumingImpl(data);
  }

  @Override
  public void flush() {
    if (printer != null) {
      printer.flush();
    }
  }

  @Override
  public void close() {
    try {
      if (printer == null) {
        initPrinter(null);
      }
      preClosePrinter();
    } finally {
      assert printer != null;
      printer.close();
    }
  }

  // private helpers -------------------------------------------------------------------------------------------------

  protected void initPrinter(Object data) {
    if (uri == null) {
      throw ExceptionFactory.getInstance().configurationError("Property 'uri' not set on bean " + getClass().getName());
    }
    wasAppended = (append && IOUtil.isURIAvailable(uri));

    // check if path exists, if not make sure it exists
    File directory = new File(uri);
    if (!wasAppended
        && directory.getParent() != null
        && !directory.isDirectory()
        && !directory.getParentFile().exists()) {
      boolean result = directory.getParentFile().mkdirs();
      if (!result) {
        throw ExceptionFactory.getInstance().configurationError("filepath does not exist and can not be created ...");
      }
    }

    printer = IOUtil.getPrinterForURI(uri, encoding, append, lineSeparator, true);
    postInitPrinter(data);
  }

  protected void println() {
    printer.print(lineSeparator);
  }

  // java.lang.Object overrides --------------------------------------------------------------------------------------

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[" + uri + "]";
  }

}
