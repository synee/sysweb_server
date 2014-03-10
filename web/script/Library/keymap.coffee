$(->
    keyStack = []

    s = {
        cmd: "metaKey",
        ctrl: "ctrlKey",
        shift: "shiftKey",
        alt: "altKey"
    };
    F = {
        escape: "esc",
        control: "ctrl",
        command: "cmd",
        "break": "pause",
        windows: "cmd",
        option: "alt",
        caps_lock: "caps",
        apostrophe: "'",
        semicolon: ";",
        tilde: "~",
        accent: "`",
        scroll_lock: "scroll",
        num_lock: "num"
    };
#    r = { "/": "?", ".": ">", ",": "<",
#          "'": '"', ";": ":", "[": "{",
#          "]": "}", "\\": "|", "`": "~",
#          "=": "+", "-": "_",
#          1: "!", 2: "@", 3: "#", 4: "$", 5: "%",
#          6: "^", 7: "&", 8: "*", 9: "(", "0": ")"
#    };
    q = {
        "0": "\\", 8: "backspace", 9: "tab", 12: "num", 13: "enter",
        16: "shift", 17: "ctrl", 18: "alt", 19: "pause", 20: "caps",
        27: "esc", 32: "space", 33: "pageup", 34: "pagedown", 35: "end",
        36: "home", 37: "left", 38: "up", 39: "right", 40: "down",
        44: "print", 45: "insert", 46: "delete",
        48: "0", 49: "1", 50: "2", 51: "3", 52: "4", 53: "5", 54: "6", 55: "7", 56: "8", 57: "9",
        65: "a", 66: "b", 67: "c", 68: "d", 69: "e", 70: "f", 71: "g", 72: "h", 73: "i", 74: "j",
        75: "k", 76: "l", 77: "m", 78: "n", 79: "o", 80: "p", 81: "q", 82: "r", 83: "s", 84: "t",
        85: "u", 86: "v", 87: "w", 88: "x", 89: "y", 90: "z",
        91: "cmd", 92: "cmd", 93: "cmd",
        96: "num_0", 97: "num_1", 98: "num_2", 99: "num_3", 100: "num_4",
        101: "num_5", 102: "num_6", 103: "num_7", 104: "num_8", 105: "num_9",
        106: "num_multiply", 107: "num_add", 108: "num_enter", 109: "num_subtract", 110: "num_decimal", 111: "num_divide",
        124: "print", 144: "num", 145: "scroll",
        186: ";", 187: "=", 188: ",", 189: "-", 190: ".", 191: "/", 192: "`", 219: "[", 220: "\\", 221: "]", 222: "'", 223: "`",
        224: "cmd", 225: "alt", 57392: "ctrl", 63289: "num"
    };

    window.KeyBoardMaps = {

        _callbacks: {}

        _combes: {}

        register: (combe, callback, ctx = @)->
            callback._listenerSequence = callback._listenerSequence || (new Date().getTime() + Math.random() + "")
            combe = @_combes[combe] = @_combes[combe] || []
            @_callbacks[callback._listenerSequence] = {callback: callback, ctx: ctx}
            combe.push(callback._listenerSequence)
            @


        get: (combe)->
            self = @
            if(@_combes[combe])
                @_combes[combe].map((sequence)-> self._callbacks[sequence] )

        remove: (combe, callback, ctx = @)->
            sequence = callback._listenerSequence
            combearr = @_combes[combe] = @_combes[combe] || []
            @_combes[combe] = combearr.slice(0, combearr.indexOf(sequence)).concat(combearr.slice(combearr.indexOf(sequence) + 1))
    }

    $(document).on("keydown", (e)->
        keyStack = keyStack.slice(0,
            keyStack.indexOf(e.keyCode)).concat(keyStack.slice(keyStack.indexOf(e.keyCode) + 1)) while keyStack.indexOf(e.keyCode) >= 0
        keyStack.push(e.keyCode)
        triggerString = ''
        triggerString += q[key] + "+" for key in keyStack
        triggerString = triggerString.substr(0, triggerString.length - 1)
        if (cbs = KeyBoardMaps.get(triggerString))
            for evt in cbs
                setTimeout(->
                    evt.callback.apply(evt.ctx, e)
                , 1)
            return false
        else
            return true
    )
    $(document).on("keyup", (e)->
        keyStack = []
        return false
    )
)