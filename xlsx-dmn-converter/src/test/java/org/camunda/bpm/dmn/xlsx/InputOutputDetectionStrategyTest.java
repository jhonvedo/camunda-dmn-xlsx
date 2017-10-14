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
package org.camunda.bpm.dmn.xlsx;

import java.io.InputStream;
import java.util.Collections;

import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.camunda.bpm.model.dmn.instance.DecisionTable;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Thorben Lindhauer
 *
 */
public class InputOutputDetectionStrategyTest {

  @Test
  public void testStaticDetectionStrategy() {
    XlsxConverter converter = new XlsxConverter();
    converter.setIoDetectionStrategy(new StaticInputOutputDetectionStrategy(Collections.singleton("B"), Collections.singleton("D")));
    InputStream inputStream = TestHelper.getClassPathResource("test2.xlsx");
    DmnModelInstance dmnModelInstance = converter.convert(inputStream,false);
    Assert.assertNotNull(dmnModelInstance);

    DecisionTable table = TestHelper.assertAndGetSingleDecisionTable(dmnModelInstance);
    Assert.assertNotNull(table);
    Assert.assertEquals(1, table.getInputs().size());
    Assert.assertEquals(1, table.getOutputs().size());
    Assert.assertEquals(4, table.getRules().size());
  }

}
