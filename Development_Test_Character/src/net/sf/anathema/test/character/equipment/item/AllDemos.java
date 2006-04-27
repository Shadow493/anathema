package net.sf.anathema.test.character.equipment.item;

import net.sf.anathema.test.character.equipment.statscreation.EquipmentStatisticsCreationWizardDemo;
import de.jdemo.framework.DemoSuite;
import de.jdemo.framework.IDemo;

public class AllDemos {

  public static IDemo suite() {
    DemoSuite suite = new DemoSuite("Demo for net.sf.anathema.test.character.equipment.item"); //$NON-NLS-1$
    //$JDemo-BEGIN$
    suite.addDemo(new DemoSuite(EquipmentItemDataViewDemo.class));
    //$JDemo-END$
    return suite;
  }
}