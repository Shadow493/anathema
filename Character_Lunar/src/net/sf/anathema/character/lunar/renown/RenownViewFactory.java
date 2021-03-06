package net.sf.anathema.character.lunar.renown;

import net.sf.anathema.character.generic.additionaltemplate.IAdditionalModel;
import net.sf.anathema.character.generic.framework.additionaltemplate.IAdditionalViewFactory;
import net.sf.anathema.character.generic.type.ICharacterType;
import net.sf.anathema.character.lunar.renown.presenter.IRenownModel;
import net.sf.anathema.character.lunar.renown.presenter.RenownPresenter;
import net.sf.anathema.character.lunar.renown.view.RenownView;
import net.sf.anathema.lib.gui.IView;
import net.sf.anathema.lib.resources.IResources;

public class RenownViewFactory implements IAdditionalViewFactory {

  public IView createView(IAdditionalModel model, IResources resources, ICharacterType type) {
    RenownView view = new RenownView();
    new RenownPresenter((IRenownModel) model, view, resources).initPresentation();
    return view;
  }
}