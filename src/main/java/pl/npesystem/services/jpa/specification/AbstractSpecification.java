/**
 * Copyright © 2019, Wen Hao <wenhao@126.com>.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package pl.npesystem.services.jpa.specification;

import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import java.io.Serializable;

abstract class AbstractSpecification<T> implements Specification<T>, Serializable {
    public From getRoot(String property, Root<T> root) {
        if (property.contains(".")) {
            String[] properties = StringUtils.split(property, ".");
            From join = root;
            for (int i = 0; i < properties.length - 1; i++) {
                join = join.join(properties[i], JoinType.LEFT);
            }
            return join;
        }
        return root;
    }

    public String getProperty(String property) {
        if (property.contains(".")) {
            String[] properties = StringUtils.split(property, ".");
            return properties[properties.length - 1];
        }
        return property;
    }

    public Path<?> getNestedPath(Root<T> root, String property) {
        String[] nestedProperties = property.split("\\.");
        Path<?> path = root;
        for (String nestedProperty : nestedProperties) {
            path = path.get(nestedProperty);
        }
        return path;
    }
}
