import './../style/style.css'
import SearchInput from "../components/SearchInput.jsx";
import Tag from "../components/Tag.jsx";
import AutoSaveTextArea from "../components/AutoSaveTextArea.jsx";
import {useAppDispatch, useAppSelector} from "../redux/Store.js";
import {useEffect, useState} from "react";
import {
    fetchAddNoteData, fetchAddTagData,
    fetchChangeFolderNoteData,
    fetchChangeNoteData,
    fetchCreateFolderNoteData,
    fetchDeleteFolderNoteData,
    fetchNotesData,
    fetchRemoveNoteData, fetchRemoveTagData
} from "../redux/slices/NoteReducer.js";
import {fetchLogout, fetchUserData} from "../redux/slices/AuthReducer.js";
import ListNotes from "../components/ListNotes.jsx";
import {useNavigate, useParams} from "react-router-dom";
import {useLocalStorage} from "../hooks/HookLocalStorage.js";
import TagInput from "../components/TagInput.jsx";

const Home = () => {

    const dispatch = useAppDispatch();
    const parameters = useParams();
    const paramId = Number(parameters.id);
    const navigate = useNavigate();

    const notes = useAppSelector(state => state.notes)?.data;
    const user = useAppSelector(state => state.auth)?.data;

    const selectedNote = useAppSelector(state => state.notes.data.find(note => note.id === paramId));
    const [isPanelHidden, setIsPanelHidden] = useState(false);
    const [isSettingsHidden, setIsSettingsHidden] = useState(true);
    const [localStorage, _] = useLocalStorage();
    const [listNotes, setListNotes] = useState([]);

    useEffect(() => {
        if (!localStorage.access_token) {
            navigate("/login");
        }
    }, [localStorage, navigate])

    useEffect(() => {
        dispatch(fetchNotesData());
        dispatch(fetchUserData());
    }, [dispatch]);

    const search = (value) => {
        if (value === '') {
            setListNotes([]);
        } else {
            const filterByContent = notes.filter((note) => note?.content?.includes(value));
            const filterByDate = notes.filter((note) => note?.created_at?.includes(value.replaceAll("\\.", "-")));
            const filterByStatus = notes.filter((note) => note?.status?.includes(value));
            const filter = [...filterByContent, ...filterByDate, ...filterByStatus].filter((value, index, array) => array.indexOf(value) === index);
            setListNotes(filter.length === 0 ? null : filter);
            console.log(filterByContent);
            console.log(filterByDate);
        }

    }

    const handleClickSettings = (status) => {
        dispatch(fetchChangeNoteData({
            id: paramId,
            content: selectedNote?.content,
            status: status
        }))
        setIsSettingsHidden(true);
    };
    return (
        <div className="app">
            <header className="header container">
                <div className="header_notes">
                    <h3 className="header_text" onClick={() => navigate("/")}>
                        My Notes
                    </h3>
                    <img className="img img_notes" src="/img/add.svg" alt="#"
                         onClick={() => dispatch(fetchAddNoteData())}/>
                </div>
                <div className="header_icon">
                    <img className="img" src="/img/left_panel.svg" alt="#"
                         onClick={() => setIsPanelHidden(!isPanelHidden)}/>
                    <p>Hi, {user.first_name} {user.last_name}</p>
                    <div className="flex">
                        <div className='setting-block' style={(paramId) ? {display: "flex"} : {display: "none"}}>
                            <img className="img header_settings" src="/img/settings.svg" alt="#"
                                 onClick={() => setIsSettingsHidden(!isSettingsHidden)}/>
                            <div className='setting-container'
                                 style={(isSettingsHidden) ? {display: "none"} : {display: "block"}}>
                                <button className='button' disabled={(selectedNote?.status === "default")}
                                        onClick={() => handleClickSettings("default")}>default
                                </button>
                                <button className='button' disabled={(selectedNote?.status === "important")}
                                        onClick={() => handleClickSettings("important")}>important
                                </button>
                                <button className='button' disabled={(selectedNote?.status === "significant")}
                                        onClick={() => handleClickSettings("significant")}>significant
                                </button>
                            </div>
                        </div>
                        <img className="img" src="/img/logout.svg" alt="#" onClick={() => {
                            dispatch(fetchLogout());
                            navigate("/login");
                        }}/>
                    </div>
                </div>
            </header>
            <section className="container section_notes">
                <div className="block_left" style={(isPanelHidden) ? {display: "none"} : {display: "block"}}>
                    <SearchInput onSearch={(value) => search(value)}/>
                    <div className="notes_block-left">
                        <ListNotes notes={listNotes === null ? [] : (listNotes.length === 0 ? notes : listNotes)}
                                   onFolderCreate={(note_id, folder) => dispatch(fetchCreateFolderNoteData({
                                       note_id: note_id,
                                       name: folder.name
                                   }))}
                                   onFolderChange={(note_id, folder) => dispatch(fetchChangeFolderNoteData({
                                       note_id: note_id,
                                       id: folder.id
                                   }))}
                                   onFolderDelete={(note_id) => dispatch(fetchDeleteFolderNoteData({note_id: note_id}))}
                                   onNoteDelete={(id) => dispatch(fetchRemoveNoteData({id: id})).then(() => navigate("/"))}
                                   onNoteClick={(url) => navigate(url)}/>
                    </div>
                </div>

                <div className="block_right"
                     style={(isPanelHidden) ? {width: "1440px", borderLeft: "1px solid rgba(220, 220, 222, 1)"} : {}}>
                    <div className="input_block">
                        <AutoSaveTextArea disabled={isNaN(paramId)}
                                          onSave={(content) => dispatch(fetchChangeNoteData({
                                              id: paramId,
                                              content: content,
                                              status: selectedNote?.status
                                          }))}
                                          content={selectedNote?.content}/>
                    </div>
                    <div className="tag_section" style={(isPanelHidden) ? {width: "1410px"} : {}}>
                        <TagInput onAdd={(tag) => {
                            dispatch(fetchAddTagData({note_id: paramId, value: tag}));
                        }}/>
                        <div className="tag_block">
                            {notes.find((note) => note.id === paramId)?.tags?.map((tag) => {
                                return (
                                    <Tag key={tag.id}
                                         onDelete={(id) => dispatch(fetchRemoveTagData({note_id: paramId, id: id}))}
                                         tag={tag}/>
                                );
                            })}
                        </div>
                    </div>
                </div>
            </section>
        </div>
    );
}

export default Home;