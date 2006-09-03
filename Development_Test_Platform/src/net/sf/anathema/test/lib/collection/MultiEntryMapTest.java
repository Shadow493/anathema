package net.sf.anathema.test.lib.collection;

import junit.framework.Assert;
import net.sf.anathema.lib.collection.MultiEntryMap;

import org.junit.Test;

public class MultiEntryMapTest {

  @Test
  public void testContainsValue() {
    MultiEntryMap<String, String> map = new MultiEntryMap<String, String>();
    Assert.assertFalse(map.containsValue("Value")); //$NON-NLS-1$
    map.add("Expected", "Value");  //$NON-NLS-1$//$NON-NLS-2$
    Assert.assertTrue(map.containsValue("Value")); //$NON-NLS-1$
  }
}