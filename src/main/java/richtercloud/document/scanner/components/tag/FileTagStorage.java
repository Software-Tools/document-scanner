/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package richtercloud.document.scanner.components.tag;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author richter
 */
public class FileTagStorage implements TagStorage {
    private final File file;

    public FileTagStorage(File file) {
        this.file = file;
    }

    @Override
    public Set<String> getAvailableTags() throws TagRetrievalException {
        Set<String> retValue = new HashSet<>();
        if(!file.exists()) {
            return retValue;
        }
        try (
            InputStream inputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ) {
            String tag = objectInputStream.readUTF();
            while(tag != null) {
                retValue.add(tag);
                tag = objectInputStream.readUTF();
            }
        } catch(EOFException ex) {
            //expected EOF
        } catch (IOException ex) {
            throw new TagRetrievalException(ex);
        }
        return retValue;
    }

    @Override
    public void addTag(String tag) throws TagRetrievalException {
        Set<String> tags = getAvailableTags();
        tags.add(tag);
        try {
            storeTags(tags);
        } catch (IOException ex) {
            throw new TagRetrievalException(ex);
        }
    }

    @Override
    public void removeTag(String tag) throws TagRetrievalException {
        Set<String> tags = getAvailableTags();
        tags.remove(tag);
        try {
            storeTags(tags);
        } catch (IOException ex) {
            throw new TagRetrievalException(ex);
        }
    }

    private void storeTags(Set<String> tags) throws IOException {
        if(!file.exists()) {
            Files.createFile(Paths.get(file.toURI()));
        }
        try (
            OutputStream outputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        ) {
            for(String tag0 : tags) {
                objectOutputStream.writeUTF(tag0);
            }
        }
    }
}
