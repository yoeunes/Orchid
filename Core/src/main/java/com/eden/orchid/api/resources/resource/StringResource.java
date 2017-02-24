package com.eden.orchid.api.resources.resource;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.OrchidReference;

/**
 * A Resource type that provides a plain String as content to a template. When used with renderTemplate() or renderString(),
 * this resource will supply the `page.content` variable to the template renderer. When used with renderRaw(), the raw
 * plain String content will be written directly instead.
 */
public final class StringResource extends OrchidResource {

    public StringResource(String content, OrchidReference reference) {
        super(reference);
        if(content != null) {
            EdenPair<String, JSONElement> parsedContent = getContext().getTheme().getEmbeddedData(content);
            this.rawContent = content;
            this.content = parsedContent.first;
            this.embeddedData = parsedContent.second;

            this.shouldPrecompile = (this.embeddedData != null);
        }
        else {
            this.rawContent = "";
            this.content = "";
            this.embeddedData = null;

            this.shouldPrecompile = false;
        }
    }

    public StringResource(OrchidContext context, String name, String content) {
        this(content, new OrchidReference(context, name));
    }
}
