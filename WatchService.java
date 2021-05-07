package com.payroll;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.HashMap;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class WatchService {
    private java.nio.file.WatchService watchService;
    private HashMap<WatchKey, Path> directoryWatch;

    public WatchService(Path dir) throws IOException {
        this.watchService = FileSystems.getDefault().newWatchService();
        this.directoryWatch = new HashMap<WatchKey, Path>();
        scanAndRegisterDirectory(dir);
    }

    private void scanAndRegisterDirectory(Path start) throws IOException {
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
           @Override
           public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attributes) throws IOException {
               registerDirWatchers(dir);
               return FileVisitResult.CONTINUE;
           }
        });
    }

    private void registerDirWatchers(Path dir) throws IOException {
        WatchKey key = dir.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        directoryWatch.put(key, dir);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void processEvents() {
        while (true) {
            WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException e) {
                return;
            }
            Path dir = directoryWatch.get(key);
            if (dir == null) continue;
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();
                Path name = ((WatchEvent<Path>) event).context();
                Path child = dir.resolve(name);
                System.out.format("%s : %s\n", event.kind().name(), child);

                if(kind == ENTRY_CREATE) {
                    try {
                        if (Files.isDirectory(child)) scanAndRegisterDirectory(child);
                    } catch (IOException e) {
                    }
                } else if ( kind.equals(ENTRY_DELETE)) {
                    if(Files.isDirectory(child)) directoryWatch.remove(child);
                }
            }

            boolean valid = key.reset();
            if(!valid) {
                directoryWatch.remove(key);
                if (directoryWatch.isEmpty())
                    break;
            }
        }
    }
}
