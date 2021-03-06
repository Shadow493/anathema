package net.sf.anathema.character.reporting.pdf.rendering.boxes.magic;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import net.disy.commons.core.predicate.IPredicate;
import net.disy.commons.core.util.CollectionUtilities;
import net.sf.anathema.character.generic.character.IGenericCharacter;
import net.sf.anathema.character.generic.magic.IMagic;
import net.sf.anathema.character.generic.magic.IMagicStats;
import net.sf.anathema.character.generic.traits.ITraitType;
import net.sf.anathema.character.reporting.pdf.content.ReportContent;
import net.sf.anathema.character.reporting.pdf.content.magic.GenericCharmContent;
import net.sf.anathema.character.reporting.pdf.content.magic.GenericCharmUtilities;
import net.sf.anathema.character.reporting.pdf.rendering.boxes.EncodingMetrics;
import net.sf.anathema.character.reporting.pdf.rendering.extent.Bounds;
import net.sf.anathema.character.reporting.pdf.rendering.general.table.AbstractTableEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.graphics.SheetGraphics;
import net.sf.anathema.character.reporting.pdf.rendering.graphics.TableCell;
import net.sf.anathema.character.reporting.pdf.rendering.page.IVoidStateFormatConstants;
import net.sf.anathema.lib.resources.IResources;

import java.util.Arrays;
import java.util.List;

public class GenericCharmTableEncoder extends AbstractTableEncoder<ReportContent> {

  private final IResources resources;

  public GenericCharmTableEncoder(IResources resources) {
    this.resources = resources;
  }

  public float getRequestedHeight(SheetGraphics graphics, float width, ReportContent reportContent) {
    EncodingMetrics metrics = EncodingMetrics.From(graphics, reportContent);
    return new PreferredGenericCharmHeight().getValue(metrics, width);
  }

  private GenericCharmContent createContent(ReportContent content) {
    return content.createSubContent(GenericCharmContent.class);
  }

  public boolean hasContent(ReportContent content) {
    return createContent(content).hasContent() && GenericCharmUtilities.hasDisplayedGenericCharms(content);
  }

  @Override
  protected PdfPTable createTable(SheetGraphics graphics, ReportContent content, Bounds bounds) throws DocumentException {
    IGenericCharacter character = content.getCharacter();
    PdfContentByte directContent = graphics.getDirectContent();
    List<ITraitType> traits = GenericCharmUtilities.getGenericCharmTraits(character);
    Font font = graphics.createTableFont();
    PdfTemplate learnedTemplate = createCharmDotTemplate(directContent, BaseColor.BLACK);
    PdfTemplate notLearnedTemplate = createCharmDotTemplate(directContent, BaseColor.WHITE);
    PdfPTable table = new PdfPTable(createColumnWidths(traits.size() + 1));
    table.setWidthPercentage(100);
    table.addCell(new TableCell(new Phrase(), Rectangle.NO_BORDER));
    for (ITraitType trait : traits) {
      table.addCell(createHeaderCell(graphics, directContent, trait));
    }
    for (IMagicStats stats : character.getGenericCharmStats()) {
      if (!GenericCharmUtilities.shouldShowCharm(stats, character, traits))
      	continue;
      Phrase charmPhrase = new Phrase(stats.getNameString(resources), font);
      table.addCell(new TableCell(charmPhrase, Rectangle.NO_BORDER));
      String genericId = stats.getName().getId();
      for (ITraitType trait : traits) {
        table.addCell(createGenericCell(character, trait, genericId, learnedTemplate, notLearnedTemplate));
      }
    }
    return table;
  }

  private PdfTemplate createCharmDotTemplate(PdfContentByte directContent, BaseColor color) {
    float lineWidth = 0.75f;
    float templateSize = IVoidStateFormatConstants.SMALL_SYMBOL_HEIGHT - 1 + 2 * lineWidth;
    PdfTemplate template = directContent.createTemplate(templateSize, templateSize);
    template.setColorFill(color);
    template.setColorStroke(BaseColor.BLACK);
    template.setLineWidth(lineWidth);
    float radius = templateSize / 2 - lineWidth;
    template.circle(templateSize / 2, templateSize / 2, radius);
    template.fillStroke();
    return template;
  }

  private PdfPCell createGenericCell(IGenericCharacter character, ITraitType type, String genericId, PdfTemplate learnedTemplate, PdfTemplate notLearnedTemplate) throws DocumentException {
    final String charmId = genericId + "." + type.getId(); //$NON-NLS-1$
    List<IMagic> allLearnedMagic = character.getAllLearnedMagic();
    boolean isLearned = CollectionUtilities.getFirst(allLearnedMagic, new IPredicate<IMagic>() {
      public boolean evaluate(IMagic value) {
        return charmId.equals(value.getId());
      }
    }) != null;
    Image image = Image.getInstance(isLearned ? learnedTemplate : notLearnedTemplate);
    TableCell tableCell = new TableCell(image);
    tableCell.setPadding(0);
    return tableCell;
  }

  private PdfPCell createHeaderCell(SheetGraphics graphics, PdfContentByte directContent, ITraitType abilityType) throws DocumentException {
    directContent.setColorStroke(BaseColor.BLACK);
    directContent.setColorFill(BaseColor.BLACK);
    String text = resources.getString(abilityType.getId());
    if (text.length() >= IVoidStateFormatConstants.TYPE_LONG_FORM_CUTOFF) {
      text = resources.getString(abilityType.getId() + ".Short");
    }
    BaseFont baseFont = graphics.getBaseFont();
    float ascentPoint = baseFont.getAscentPoint(text, IVoidStateFormatConstants.TABLE_FONT_SIZE);
    float descentPoint = baseFont.getDescentPoint(text, IVoidStateFormatConstants.TABLE_FONT_SIZE);
    float templateWidth = baseFont.getWidthPoint(text, IVoidStateFormatConstants.TABLE_FONT_SIZE);
    float templateHeight = ascentPoint - descentPoint;

    PdfTemplate template = directContent.createTemplate(templateWidth, templateHeight);
    template.beginText();
    template.setFontAndSize(baseFont, IVoidStateFormatConstants.TABLE_FONT_SIZE);
    template.showTextAligned(Element.ALIGN_LEFT, text, 0, -descentPoint, 0);
    template.endText();
    Image image = Image.getInstance(template);
    image.setRotationDegrees(90);
    TableCell tableCell = new TableCell(image);
    tableCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
    tableCell.setPaddingTop(5);
    return tableCell;
  }

  private float[] createColumnWidths(int columnCount) {
    float[] columnWidths = new float[columnCount];
    Arrays.fill(columnWidths, 1);
    columnWidths[0] = (int) (.4 * columnCount);
    return columnWidths;
  }
}
