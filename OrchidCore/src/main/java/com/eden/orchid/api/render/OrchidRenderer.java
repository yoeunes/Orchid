package com.eden.orchid.api.render;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.apache.commons.io.FilenameUtils;

import javax.inject.Inject;

public abstract class OrchidRenderer {

    protected OrchidContext context;
    protected OrchidResources resources;
    protected TemplateResolutionStrategy strategy;

    @Inject
    public OrchidRenderer(OrchidContext context, OrchidResources resources, TemplateResolutionStrategy strategy) {
        this.context = context;
        this.resources = resources;
        this.strategy = strategy;
    }

    /**
     * Render the given page with the given template resource. A list of templates can be given, and the first resource
     * that could be located will be the template used.
     *
     * @param page the page to render
     * @return true if the page was successfully rendered, false otherwise
     */
    public final boolean renderTemplate(OrchidPage page) {
        for (String template : strategy.getPageTemplate(page)) {
            OrchidResource templateResource = resources.getResourceEntry(template);

            if (templateResource != null) {
                return render(page, FilenameUtils.getExtension(template), templateResource.getContent());
            }
        }

        return false;
    }

    /**
     * Render the given page using a literal String as a template.
     *
     * @param page the page to render
     * @param extension the extension that the content represents and should be compiled against
     * @param templateString the template string to render
     * @return true if the page was successfully rendered, false otherwise
     */
    public final boolean renderString(OrchidPage page, String extension, String templateString) {
        return render(page, extension, templateString);
    }

    /**
     * Render the content of a page directly, without any template.
     *
     * @param page the page to render
     * @return true if the page was successfully rendered, false otherwise
     */
    public final boolean renderRaw(OrchidPage page) {
        String content = page.getResource().getContent();

        if(page.getResource().shouldPrecompile()) {
            content = context.precompile(content, page.getData());
        }

        return render(page, page.getResource().getReference().getExtension(), content);
    }

    /**
     * Internal representation of a 'render' operation.
     *
     * @param page the page to render
     * @param extension the extension that the content represents and should be compiled against
     * @param content the template string to render
     * @return true if the page was successfully rendered, false otherwise
     */
    protected abstract boolean render(OrchidPage page, String extension, String content);

}
