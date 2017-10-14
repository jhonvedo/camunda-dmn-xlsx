/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.dmn.xlsx.cli;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.camunda.bpm.dmn.xlsx.InputOutputDetectionStrategy;
import org.camunda.bpm.dmn.xlsx.StaticInputOutputDetectionStrategy;
import org.camunda.bpm.dmn.xlsx.XlsxConverter;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelInstance;

/**
 * @author Thorben Lindhauer
 *
 */
public class CommandLineConverter {

  public static void main(String[] args) {      
     
    boolean haveTypes = false;
      
    if (args.length == 0) {
      StringBuilder sb = new StringBuilder();
      sb.append("Usage: java -jar ...jar [--inputs A,B,C,..] [--outputs D,E,F,...] path/to/file.xlsx path/to/outfile.dmn");
      System.out.println(sb.toString());
      return;
    }

    String inputFile = args[args.length - 2];
    String outputFile = args[args.length - 1];


    Set<String> inputs = null;
    Set<String> outputs = null;
    for (int i = 0; i < args.length - 2; i++) {
      if ("--inputs".equals(args[i])) {
        inputs = new HashSet<String>();
        inputs.addAll(getHeaders(args[i + 1]));
      }
      if ("--have-types".equals(args[i])) {
       haveTypes = (args[i + 1].trim().toUpperCase().equals("TRUE"));
      }
      

      if ("--outputs".equals(args[i])) {
        outputs = new HashSet<String>();
        outputs.addAll(getHeaders(args[i + 1]));
      }
    }

    XlsxConverter converter = new XlsxConverter();
    if (inputs != null && outputs != null) {
      InputOutputDetectionStrategy ioStrategy = new StaticInputOutputDetectionStrategy(inputs, outputs);
      converter.setIoDetectionStrategy(ioStrategy);
    }

    FileInputStream fileInputStream = null;
    FileOutputStream fileOutputStream = null;
    try {
      try {

        fileInputStream = new FileInputStream(inputFile);
        DmnModelInstance dmnModelInstance = converter.convert(fileInputStream,haveTypes);
        fileOutputStream = new FileOutputStream(outputFile);
        Dmn.writeModelToStream(fileOutputStream, dmnModelInstance);
      }
      finally {
        if (fileInputStream != null) {
          fileInputStream.close();
        }

        if (fileOutputStream != null) {
          fileOutputStream.close();
        }
      }
    } catch (Exception e) {
      System.out.println("Could not convert file: " + e.getMessage());
      e.printStackTrace(System.out);
    }

  }

    private static Collection<String> getHeaders(String arg) {
       if(arg.contains("-") && arg.length()==3){
           char[] args = arg.replace("-", "").toCharArray();
           ArrayList<String> headers = new ArrayList<String>();
           for (char i = args[0]; i <= args[1]; i++) {               
               headers.add(String.valueOf(i));
           }
           return headers;          
       }
       else{
           return Arrays.asList(arg.split(","));
       }
    }
}
