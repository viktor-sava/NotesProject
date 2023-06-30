import Folder from "./Folder.jsx";
import Note from "./Note.jsx";
import PropTypes from "prop-types";
import {useEffect, useRef, useState} from "react";
import './../style/style.css'

const ListNotes = (props) => {

    const {notes} = props;
    const [currentNote, setCurrentNote] = useState();
    const [isTyping, setIsTyping] = useState(false);
    const [newFolderContent, setNewFolderContent] = useState('');
    const inputRef = useRef();

    const getFolders = () => {
        return notes.map((note) => note?.folder).filter(
            (folder, i, folders) => folders?.findIndex(t => t?.id === folder?.id) === i
        );
    }

    const getNotes = (folderId) => {
        if (folderId === null) {
            return notes.filter(note => note.folder === null);
        }
        return notes.filter(note => note.folder?.id === folderId);
    }

    useEffect(() => {
        if (inputRef.current && isTyping) {
            inputRef.current.focus();
        }
    }, [isTyping]);

    const handleKeyDown = (e) => {
        if (e.code === "Enter") {
            if (currentNote) {
                props.onFolderCreate(currentNote.id, {name: newFolderContent});
                setNewFolderContent("");
                setIsTyping(false);
            }
        } else if (e.code === "Escape") {
            if (currentNote) {
                props.onFolderDelete(currentNote.id);
                setNewFolderContent("");
                setIsTyping(false);
            }
        }
    };

    return (<>
        <div className="folder_container">
            {getFolders().map((folder) => {
                if (folder !== null) {
                    return (
                        <Folder folder={folder}
                                key={folder.id} name={folder.name}
                                onDrop={(folder) => props.onFolderChange(currentNote.id, folder)}>
                            {getNotes(folder.id).map((note) => {
                                return (
                                    <Note key={note.id}
                                          note={note}
                                          onDragStart={setCurrentNote}
                                          onDelete={props.onNoteDelete}
                                          onClick={props.onNoteClick}
                                          isSelected={note.id === props.selectedNote?.id}/>
                                );
                            })}
                        </Folder>
                    );
                }
            })}
        </div>
        {getNotes(null).map((note) => {
            return (
                <Note key={note.id}
                      note={note}
                      onDelete={props.onNoteDelete}
                      onDragStart={setCurrentNote}
                      onClick={props.onNoteClick}
                      isSelected={note.id === props.selectedNote?.id}/>
            );
        })}
        <input value={newFolderContent}
               maxLength="30"
               ref={inputRef}
               onChange={(e) => setNewFolderContent(e.target.value)}
               onKeyDown={(e) => handleKeyDown(e)}
               style={{display: isTyping ? 'block' : 'none'}}
               className="folder_input"
               placeholder="Write folder name..."/>
        <div className="space"
             onDragOver={(e) => e.preventDefault()}
             onDrop={(e) => {
                 e.preventDefault();
                 setIsTyping(true);
             }}></div>
    </>);
}

ListNotes.propTypes = {
    notes: PropTypes.array.isRequired,
    selectedNote: PropTypes.object,
    onFolderChange: PropTypes.func.isRequired,
    onFolderCreate: PropTypes.func.isRequired,
    onFolderDelete: PropTypes.func.isRequired,
    onNoteDelete: PropTypes.func.isRequired,
    onNoteClick: PropTypes.func.isRequired,
}

export default ListNotes;