package com.eden.orchid.impl.compilers.frontmatter;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidPreCompiler;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class FrontMatterPrecompiler extends OrchidPreCompiler {

    private OrchidContext context;

    private static final String defaultType = "yml";

    @Inject
    public FrontMatterPrecompiler(OrchidContext context) {
        this.context = context;
        this.priority = 100;
    }

    @Override
    public EdenPair<String, JSONElement> getEmbeddedData(String input) {
        EdenPair<JSONObject, Integer> frontMatter = parseFrontMatter(input);

        String result;
        if(frontMatter.second != 0) {
            JSONObject root = new JSONObject(context.getRoot().toMap());

            for (String key : frontMatter.first.keySet()) {
                root.put(key, frontMatter.first.get(key));
            }

            result = context.getTheme().compile("twig", input.substring(frontMatter.second), root);
        }
        else {
            result = input;
        }

        return new EdenPair<>(result, new JSONElement(frontMatter.first));
    }

    private EdenPair<JSONObject, Integer> parseFrontMatter(String input) {
        if(input.startsWith("---")) {
            Matcher m = Pattern.compile("^---(\\w+)?$", Pattern.MULTILINE).matcher(input);

            int matches = 0;
            int fmStart = 0;
            int fmEnd = 0;

            int contentStart = 0;

            // if we find a match, get the group
            while (m.find()) {
                if (matches == 0) {
                    fmStart = m.end();
                    matches++;
                }
                else if (matches == 1) {
                    fmEnd = m.start();
                    contentStart = m.end();
                    matches++;
                    break;
                }
            }

            if (matches == 2) {
                String parsedType = input.substring(0, fmStart).replaceAll("---", "");

                List<String> extensions = new ArrayList<>();
                if(!EdenUtils.isEmpty(parsedType)) {
                    extensions.add(parsedType);
                }

                if(!EdenUtils.isEmpty(context.query("options.frontMatterExt"))) {
                    extensions.add(context.query("options.frontMatterExt").toString());
                }

                extensions.add(defaultType);

                final String frontMatterText = input.substring(fmStart, fmEnd);

                JSONObject frontMatter = extensions
                        .stream()
                        .map(ext -> context.getTheme().parse(ext, frontMatterText))
                        .filter(OrchidUtils::elementIsObject)
                        .map(el -> (JSONObject) el.getElement())
                        .findFirst()
                        .orElseGet(JSONObject::new);

                return new EdenPair<>(frontMatter, contentStart);
            }
        }

        return new EdenPair<>(null, 0);
    }

}

