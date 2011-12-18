package net.sf.anathema.character.reporting.pdf.layout.extended;

import com.lowagie.text.Anchor;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import net.sf.anathema.character.reporting.pdf.content.ReportContent;
import net.sf.anathema.character.reporting.pdf.rendering.elements.Bounds;
import net.sf.anathema.character.reporting.pdf.rendering.general.PdfHorizontalLineContentEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.general.PdfTextEncodingUtilities;
import net.sf.anathema.character.reporting.pdf.rendering.general.box.IBoxContentEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.general.box.IVariableBoxContentEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.general.box.PdfBoxEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.page.IPdfPageEncoder;
import net.sf.anathema.character.reporting.pdf.rendering.page.IVoidStateFormatConstants;
import net.sf.anathema.character.reporting.pdf.rendering.page.PdfPageConfiguration;
import net.sf.anathema.lib.resources.IResources;

public abstract class AbstractPdfPageEncoder implements IPdfPageEncoder {
  private final IResources resources;
  private final BaseFont baseFont;

  private final ExtendedEncodingRegistry registry;
  private final PdfPageConfiguration pageConfiguration;
  private final IExtendedPartEncoder partEncoder;
  private final PdfBoxEncoder boxEncoder;

  public AbstractPdfPageEncoder(IExtendedPartEncoder partEncoder, ExtendedEncodingRegistry registry, IResources resources,
    PdfPageConfiguration pageConfiguration) {
    this.partEncoder = partEncoder;
    this.registry = registry;
    this.baseFont = registry.getBaseFont();
    this.resources = resources;
    this.pageConfiguration = pageConfiguration;
    this.boxEncoder = new PdfBoxEncoder(resources, getBaseFont());
  }

  public abstract void encode(Document document, PdfContentByte directContent, ReportContent content) throws DocumentException;

  protected void encodeCopyright(PdfContentByte directContent) throws DocumentException {
    float lineHeight = IVoidStateFormatConstants.COMMENT_FONT_SIZE + 2f;
    Font copyrightFont = new Font(getBaseFont(), IVoidStateFormatConstants.COMMENT_FONT_SIZE);
    float copyrightHeight = getPageConfiguration().getLowerContentY();

    Bounds firstColumnBounds = getPageConfiguration().getFirstColumnRectangle(getContentHeight(), copyrightHeight, 1);
    Anchor voidstatePhrase = new Anchor("Inspired by Voidstate\nhttp://www.voidstate.com", copyrightFont); //$NON-NLS-1$
    voidstatePhrase.setReference("http://www.voidstate.com"); //$NON-NLS-1$
    PdfTextEncodingUtilities.encodeText(directContent, voidstatePhrase, firstColumnBounds, lineHeight);

    // TODO: Eliminate these hard-coded copyright dates; these should be in a properties file or something.
    Anchor anathemaPhrase = new Anchor("Created with Anathema \u00A92011\nhttp://anathema.sf.net", copyrightFont); //$NON-NLS-1$
    anathemaPhrase.setReference("http://anathema.sf.net"); //$NON-NLS-1$
    Bounds anathemaBounds = getPageConfiguration().getSecondColumnRectangle(getContentHeight(), copyrightHeight, 1);
    PdfTextEncodingUtilities.encodeText(directContent, anathemaPhrase, anathemaBounds, lineHeight, Element.ALIGN_CENTER);
    Anchor whitewolfPhrase = new Anchor("Exalted \u00A92011 by White Wolf, Inc.\nhttp://www.white-wolf.com", //$NON-NLS-1$
      copyrightFont);
    whitewolfPhrase.setReference("http://www.white-wolf.com"); //$NON-NLS-1$

    Bounds whitewolfBounds = getPageConfiguration().getThirdColumnRectangle(getContentHeight(), copyrightHeight);
    PdfTextEncodingUtilities.encodeText(directContent, whitewolfPhrase, whitewolfBounds, lineHeight, Element.ALIGN_RIGHT);
  }

  protected ExtendedEncodingRegistry getRegistry() {
    return registry;
  }

  protected IResources getResources() {
    return resources;
  }

  protected BaseFont getBaseFont() {
    return baseFont;
  }

  protected PdfPageConfiguration getPageConfiguration() {
    return pageConfiguration;
  }

  public float getContentHeight() {
    return pageConfiguration.getContentHeight();
  }

  protected IExtendedPartEncoder getPartEncoder() {
    return partEncoder;
  }

  protected PdfBoxEncoder getBoxEncoder() {
    return boxEncoder;
  }

  protected String getHeaderLabel(String key) {
    return getResources().getString("Sheet.Header." + key); //$NON-NLS-1$
  }

  protected float calculateBoxIncrement(float height) {
    return height + IVoidStateFormatConstants.PADDING;
  }

  protected float removeBoxIncrement(float height) {
    return height - IVoidStateFormatConstants.PADDING;
  }

  protected Bounds calculateBounds(int column, int span, float distanceFromTop, float height) {
    Bounds bounds = null;
    switch (column) {
      case 1:
        bounds = getPageConfiguration().getFirstColumnRectangle(distanceFromTop, height, span);
        break;
      case 2:
        bounds = getPageConfiguration().getSecondColumnRectangle(distanceFromTop, height, span);
        break;
      case 3:
        bounds = getPageConfiguration().getThirdColumnRectangle(distanceFromTop, height);
        break;
    }
    return bounds;
  }

  protected float getWidth(int column, int span) {
    switch (column) {
      case 1:
        return getPageConfiguration().getFirstColumnRectangle(0, 0, span).width;
      case 2:
        return getPageConfiguration().getSecondColumnRectangle(0, 0, span).width;
      case 3:
        return getPageConfiguration().getThirdColumnRectangle(0, 0).width;
    }
    return 0;
  }

  protected float encodeFixedBox(PdfContentByte directContent, ReportContent content, IBoxContentEncoder encoder, int column, int span,
    float distanceFromTop, float height) throws DocumentException {
    getBoxEncoder().encodeBox(content, directContent, encoder, calculateBounds(column, span, distanceFromTop, height));
    return height;
  }

  protected float encodeFixedBoxBottom(PdfContentByte directContent, ReportContent content, IBoxContentEncoder encoder, int column, int span,
    float bottom, float height) throws DocumentException {
    getBoxEncoder().encodeBox(content, directContent, encoder, calculateBounds(column, span, bottom - height, height));
    return height;
  }

  protected float encodeVariableBox(PdfContentByte directContent, ReportContent content, IVariableBoxContentEncoder encoder, int column, int span,
    float distanceFromTop, float maxHeight) throws DocumentException {
    float height = Math.min(maxHeight, boxEncoder.getRequestedHeight(encoder, content, getWidth(column, span)));
    return encodeFixedBox(directContent, content, encoder, column, span, distanceFromTop, height);
  }

  protected float encodeVariableBoxBottom(PdfContentByte directContent, ReportContent content, IVariableBoxContentEncoder encoder, int column,
    int span, float bottom, float maxHeight) throws DocumentException {
    float height = Math.min(maxHeight, boxEncoder.getRequestedHeight(encoder, content, getWidth(column, span)));
    return encodeFixedBoxBottom(directContent, content, encoder, column, span, bottom, height);
  }

  protected float encodeNotes(PdfContentByte directContent, ReportContent content, String title, int column, int span, float distanceFromTop,
    float height, int textColumns) throws DocumentException {
    IBoxContentEncoder encoder = new PdfHorizontalLineContentEncoder(textColumns, title);
    return encodeFixedBox(directContent, content, encoder, column, span, distanceFromTop, height);
  }
}